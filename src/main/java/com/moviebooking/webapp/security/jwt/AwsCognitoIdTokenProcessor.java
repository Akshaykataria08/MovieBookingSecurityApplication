package com.moviebooking.webapp.security.jwt;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebooking.webapp.config.CognitoConfig;
import com.moviebooking.webapp.exceptions.InternalServerErrorException;
import com.moviebooking.webapp.security.JwtAuthentication;
import com.moviebooking.webapp.service.CognitoService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

@Component
public class AwsCognitoIdTokenProcessor {

	@Autowired
	private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

	@Autowired
	private JwtConstants jwtConstants;

	@Autowired
	private CognitoConfig cognitoConfig;

	@Autowired
	private CognitoService cognitoService;

	public Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idToken = getIdTokenFromRequest(request);

		if (idToken != null) {
			JWTClaimsSet claims = null;
			try {
				claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
			} catch(ParseException | BadJOSEException | JOSEException e) {
				if(tokenExpired(e)) {
					String userName = getUsernameFromIdToken(idToken);
					idToken = cognitoService.refreshUserSession(userName, idToken);
					try {
						claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
						setOAuthCookie(idToken, response);
						return this.generateJwtAuthenticationObj(claims);
					} catch (ParseException | BadJOSEException | JOSEException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
			validateJwt(claims);
			return this.generateJwtAuthenticationObj(claims);
		}
		return null;
	}

	private Authentication generateJwtAuthenticationObj(JWTClaimsSet claims) throws ParseException {
		List<GrantedAuthority> grantedAuthorities = this.getGrantedAuthorities(claims);
		User user = new User(this.getUserNameFrom(claims), "", grantedAuthorities);
		return new JwtAuthentication(user, claims, grantedAuthorities);
	}

	private List<GrantedAuthority> getGrantedAuthorities(JWTClaimsSet claims) throws ParseException {
		return Stream
				.of(claims.getStringArrayClaim(JwtConstants.ROLES))
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList());
	}

	private void setOAuthCookie(String idToken, HttpServletResponse response) {
		Cookie cookie = new Cookie(JwtConstants.OAUTH_COOKIE_NAME, idToken);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	private String getUsernameFromIdToken(String idToken) {
		String claimsString = new String(Base64.getDecoder().decode(idToken.split("\\.")[1]));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> claimSet = new HashMap<>();
		try {
			claimSet = mapper.readValue(claimsString, new TypeReference<>() {});
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
			throw new InternalServerErrorException(e.getMessage());
		}
		return (String) claimSet.get(JwtConstants.USERNAME_FIELD);
	}

	private boolean tokenExpired(Exception e) {
		return e.getMessage().equals(JwtConstants.EXPIRED_JWT);
	}

	private String getIdTokenFromRequest(HttpServletRequest request) {
		String idToken = null;
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie: cookies) {
			if(cookie.getName().equals(JwtConstants.OAUTH_COOKIE_NAME)) {
				idToken = cookie.getValue();
			}
		}
		//		idToken = request.getHeader(jwtConstants.getHttpHeader());
		return idToken;
	}

	public String getUserNameFrom(JWTClaimsSet claims) {
		return claims.getClaims().get(JwtConstants.USERNAME_FIELD).toString();
	}

	public void validateJwt(JWTClaimsSet claims) throws Exception {
		if (!claims.getIssuer().equals(jwtConstants.getIssuer())) {
			throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(),
					jwtConstants.getIssuer()));
		}

		if(!claims.getAudience().contains(cognitoConfig.getClientId())) {
			throw new Exception(String.format("Audience %s does not match cognito idp %s", claims.getAudience(),
					cognitoConfig.getClientId()));
		}
	}

	public Date getExpiryDate(JWTClaimsSet claims) {
		return claims.getExpirationTime();
	}

	private String getBearerToken(String token) {
		return token.startsWith(JwtConstants.BEARER_TOKEN_TYPE) ? token.substring(JwtConstants.BEARER_TOKEN_TYPE.length())
				: token;
	}
}
