package com.moviebooking.webapp.service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.InvalidParameterException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.moviebooking.webapp.config.CognitoConfig;
import com.moviebooking.webapp.domain.LoggedInUser;
import com.moviebooking.webapp.domain.OidcTokens;
import com.moviebooking.webapp.exceptions.CustomException;
import com.moviebooking.webapp.exceptions.InternalServerErrorException;
import com.moviebooking.webapp.exceptions.UnauthorizedException;
import com.moviebooking.webapp.repository.AuthCredentialsRepository;
import com.moviebooking.webapp.requestdto.UserAuthCredentials;
import com.moviebooking.webapp.requestdto.UserChangePasswordRequest;
import com.moviebooking.webapp.requestdto.UserForgotPasswordRequest;
import com.moviebooking.webapp.requestdto.UserProfileRegistrationRequest;
import com.moviebooking.webapp.requestdto.UserResetPasswordRequest;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.UserProfileRegistrationResponse;
import com.moviebooking.webapp.responsedto.UserProfileResponse;
import com.moviebooking.webapp.security.jwt.AwsCognitoIdTokenProcessor;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.moviebooking.webapp.utility.ResponseUtility;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import reactor.core.publisher.Mono;

@Service
public class CognitoServiceImpl implements CognitoService {

	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String SECRET_HASH = "SECRET_HASH";
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

	@Value("${userProfileUrl}")
	private String userProfileUrl;

	@Autowired
	private AWSCognitoIdentityProvider cognitoClient;

	@Autowired
	private CognitoConfig cognitoConfig;

	@Autowired
	private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

	@Autowired
	private AwsCognitoIdTokenProcessor tokenProcessor;

	@Autowired
	private AuthCredentialsRepository credRepo;

	@Autowired
	private ResponseUtility responseUtility;

	@Override
	public Mono<ResponseEntity<? extends Response>> registerUser(UserAuthCredentials userAuthCredentials,
			UserProfileRegistrationRequest userProfileRegistrationRequest) throws UsernameExistsException {

		SignUpResult createUserResult = cognitoClient.signUp(createSignUpRequest(userAuthCredentials));
		String userName = createUserResult.getUserSub();
		cognitoClient.adminAddUserToGroup(createAdminAddUserToGroupRequest(userName));

		return responseUtility.sendPostRequest(userProfileUrl, "/v1/profile", userProfileRegistrationRequest,
				UserProfileRegistrationRequest.class, UserProfileRegistrationResponse.class).doOnError(e -> {
					cognitoClient.adminDeleteUser(createAdminDeleteUserRequest(userName));
					throw new InternalServerErrorException("Something went wrong");
				});
	}

	@Override
	public Mono<LoggedInUser> loginUser(UserAuthCredentials loginRequest)
			throws UserNotConfirmedException, NotAuthorizedException {

		try {
			InitiateAuthResult initiateAuthResult = cognitoClient.initiateAuth(createInitiateAuthRequest(loginRequest));
			if (StringUtils.stripToNull(initiateAuthResult.getChallengeName()) == null) {
				try {
					JWTClaimsSet claims = configurableJWTProcessor
							.process(initiateAuthResult.getAuthenticationResult().getIdToken(), null);
					saveOidcTokens(initiateAuthResult, claims);

					return getLoggedInUser(loginRequest.getEmail(), initiateAuthResult);
					
				} catch (ParseException | BadJOSEException | JOSEException e) {
					System.out.println(e.getMessage());
					throw new InternalServerErrorException("Something went wrong");
				}
			}
		} catch (UserNotConfirmedException e) {
			cognitoClient.resendConfirmationCode(createResendConfirmationCodeRequest(loginRequest.getEmail()));
			throw new UnauthorizedException("Please verify your email first");
		}

		return null;
	}

	@Override
	public String refreshUserSession(String userName, String idToken) throws Exception {

		if (!isLastIssuedIdToken(userName, idToken)) {
			throw new UnauthorizedException("Unauthorised");
		}
		InitiateAuthResult initiateAuthResult = cognitoClient
				.initiateAuth(createRefreshUserSessionRequest(userName, getRefreshToken(userName)));
		try {

			JWTClaimsSet claims = configurableJWTProcessor
					.process(initiateAuthResult.getAuthenticationResult().getIdToken(), null);
			saveOidcTokens(initiateAuthResult, claims);

		} catch (ParseException | BadJOSEException | JOSEException e) {
			System.out.println(e.getMessage());
			throw new InternalServerErrorException("Something went wrong");
		}
		return initiateAuthResult.getAuthenticationResult().getIdToken();
	}

	@Override
	public String forgotPassword(UserForgotPasswordRequest userForgotPasswordRequest) {
		try {

			if (this.isEmailVerified(userForgotPasswordRequest.getEmail())) {
				cognitoClient.forgotPassword(createForgetPasswordRequest(userForgotPasswordRequest.getEmail()));
			} else {
				cognitoClient.resendConfirmationCode(
						createResendConfirmationCodeRequest(userForgotPasswordRequest.getEmail()));
				return "Not Verified";
			}
			return "resetPassword";
		} catch (InvalidParameterException | NotAuthorizedException | UserNotConfirmedException e) {
			System.out.println("Exception Occured");
			throw new CustomException(e.getErrorMessage());
		}
	}

	@Override
	public String resetPassword(UserResetPasswordRequest user) {
		try {
			cognitoClient.confirmForgotPassword(createConfirmForgotPasswordRequest(user));
			return "Your password has been reset successfully";
		} catch (AWSCognitoIdentityProviderException e) {
			throw new CustomException(e.getErrorMessage());
		}
	}

	@Override
	public Boolean changePassword(UserChangePasswordRequest changePasswordRequest) {
		String userName = this.getUserNameFromSecurityContext();
		String accessToken = credRepo.findById(userName).orElseThrow(() -> new UnauthorizedException("UnAuthorized"))
				.getAccessToken();

		cognitoClient.changePassword(createChangePasswordRequest(accessToken, changePasswordRequest));
		return true;
	}

	@Override
	public Boolean logout() {
		String userName = this.getUserNameFromSecurityContext();
		if (!credRepo.existsById(userName)) {
			throw new UnauthorizedException("UnAuthorized");
		}
		credRepo.deleteById(userName);
		return true;
	}

	private AdminAddUserToGroupRequest createAdminAddUserToGroupRequest(String userName) {
		return new AdminAddUserToGroupRequest().withUsername(userName).withUserPoolId(cognitoConfig.getUserPoolId())
				.withGroupName(cognitoConfig.getUserGroup());
	}

	private boolean isEmailVerified(String email) {
		AdminGetUserResult adminGetUserResult = cognitoClient.adminGetUser(createAdminGetUserRequest(email));

		boolean isEmailVerified = false;
		Iterator<AttributeType> itr = adminGetUserResult.getUserAttributes().iterator();

		while (itr.hasNext()) {
			AttributeType attr = itr.next();
			if (JwtConstants.EMAIL_VERIFIED.equals(attr.getName())) {
				isEmailVerified = Boolean.parseBoolean(attr.getValue());
			}
		}
		return isEmailVerified;
	}

	private String getUserNameFromSecurityContext() {
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}

	private ChangePasswordRequest createChangePasswordRequest(String accessToken,
			UserChangePasswordRequest changePasswordRequest) {

		return new ChangePasswordRequest().withAccessToken(accessToken)
				.withPreviousPassword(changePasswordRequest.getPrevPassword())
				.withProposedPassword(changePasswordRequest.getProposedPassword());
	}

	private boolean isLastIssuedIdToken(String userName, String idToken) {
		return credRepo.findById(userName).orElseThrow(() -> new UnauthorizedException("Unauthorised")).getIdToken()
				.equals(idToken);
	}

	private String getRefreshToken(String id) {
		return credRepo.findById(id).orElseThrow(() -> new UnauthorizedException("Unauthorised")).getRefreshToken();
	}

	private InitiateAuthRequest createRefreshUserSessionRequest(String userName, String refreshToken) {

		Map<String, String> authParams = new HashMap<String, String>();
		authParams.put("REFRESH_TOKEN", refreshToken);
		authParams.put(SECRET_HASH, calculateSecretHash(userName));

		return new InitiateAuthRequest().withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
				.withClientId(cognitoConfig.getClientId()).withAuthParameters(authParams);
	}

	private ConfirmForgotPasswordRequest createConfirmForgotPasswordRequest(UserResetPasswordRequest user) {
		return new ConfirmForgotPasswordRequest().withClientId(cognitoConfig.getClientId())
				.withPassword(user.getNewPassword()).withConfirmationCode(user.getConfirmationCode())
				.withUsername(user.getEmail()).withSecretHash(calculateSecretHash(user.getEmail()));
	}

	private AdminGetUserRequest createAdminGetUserRequest(String email) {
		return new AdminGetUserRequest().withUsername(email).withUserPoolId(cognitoConfig.getUserPoolId());
	}

	private ForgotPasswordRequest createForgetPasswordRequest(String email) {
		return new ForgotPasswordRequest().withClientId(cognitoConfig.getClientId())
				.withSecretHash(calculateSecretHash(email)).withUsername(email);
	}

	private ResendConfirmationCodeRequest createResendConfirmationCodeRequest(String email) {
		return new ResendConfirmationCodeRequest().withClientId(cognitoConfig.getClientId())
				.withSecretHash(calculateSecretHash(email)).withUsername(email);
	}

	private SignUpRequest createSignUpRequest(UserAuthCredentials userAuthCredentials) {
		return new SignUpRequest().withClientId(cognitoConfig.getClientId())
				.withSecretHash(this.calculateSecretHash(userAuthCredentials.getEmail()))
				.withUsername(userAuthCredentials.getEmail()).withPassword(userAuthCredentials.getPassword())
				.withUserAttributes(this.getUserAttributes(userAuthCredentials));
	}

	private void saveOidcTokens(InitiateAuthResult initiateAuthResult, JWTClaimsSet claims) {

		OidcTokens creds = new OidcTokens();
		creds.setAccessToken(initiateAuthResult.getAuthenticationResult().getAccessToken());
		creds.setIdToken(initiateAuthResult.getAuthenticationResult().getIdToken());
		creds.setRefreshToken(initiateAuthResult.getAuthenticationResult().getRefreshToken());
		creds.setUsername(tokenProcessor.getUserNameFrom(claims));
		creds.setExpiryDate(tokenProcessor.getExpiryDate(claims));
		credRepo.save(creds);
	}

	private List<AttributeType> getUserAttributes(UserAuthCredentials userAuthCredentials) {
		List<AttributeType> userAttributes = new ArrayList<>();
		userAttributes.add(new AttributeType().withName("email").withValue(userAuthCredentials.getEmail()));
		userAttributes
				.add(new AttributeType().withName("phone_number").withValue(userAuthCredentials.getPhoneNumber()));
		return userAttributes;
	}

	private String calculateSecretHash(String userName) {
		SecretKeySpec signingKey = new SecretKeySpec(cognitoConfig.getClientSecret().getBytes(StandardCharsets.UTF_8),
				HMAC_SHA256_ALGORITHM);
		try {
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);
			mac.update(userName.getBytes(StandardCharsets.UTF_8));
			byte[] rawHmac = mac.doFinal(cognitoConfig.getClientId().getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			throw new RuntimeException("Error while calculating ");
		}
	}

	private InitiateAuthRequest createInitiateAuthRequest(UserAuthCredentials loginRequest) {

		Map<String, String> authParams = new HashMap<String, String>();
		authParams.put(USERNAME, loginRequest.getEmail());
		authParams.put(PASSWORD, loginRequest.getPassword());
		authParams.put(SECRET_HASH, calculateSecretHash(loginRequest.getEmail()));

		return new InitiateAuthRequest().withClientId(cognitoConfig.getClientId())
				.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH).withAuthParameters(authParams);
	}

	private AdminDeleteUserRequest createAdminDeleteUserRequest(String userName) {
		return new AdminDeleteUserRequest().withUsername(userName).withUserPoolId(cognitoConfig.getUserPoolId());
	}

	private Mono<LoggedInUser> getLoggedInUser(String email, InitiateAuthResult initiateAuthResult) {
		LoggedInUser loggedInUser = new LoggedInUser();
		return this.getUserProfile("/v1/profile/" + email).flatMap(userProfile -> {
			loggedInUser.setUserProfileResponse(userProfile);
			loggedInUser.setIdToken(initiateAuthResult.getAuthenticationResult().getIdToken());
			return Mono.just(loggedInUser);
		});
	}

	private Mono<UserProfileResponse> getUserProfile(String path) {
		return responseUtility
				.sendGetRequest(userProfileUrl, path, UserProfileResponse.class).doOnError(e -> {
					throw new InternalServerErrorException("Something went wrong");
				}).flatMap(userProfile -> Mono.just((UserProfileResponse) userProfile.getBody()));
	}
}
