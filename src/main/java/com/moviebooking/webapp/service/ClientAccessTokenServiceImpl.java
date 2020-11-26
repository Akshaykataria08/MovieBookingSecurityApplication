package com.moviebooking.webapp.service;

import java.text.ParseException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.moviebooking.webapp.config.CognitoConfig;
import com.moviebooking.webapp.domain.ClientAccessToken;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

@Service
public class ClientAccessTokenServiceImpl implements ClientAccessTokenService{

	@Value("${aws.cognito.auth-domain}")
	private String authDomain;
	
	@Value("${application-scopes}")
	private String applicationScopes;
	
	@Autowired
	private CognitoConfig cognitoConfig;
	
	@Autowired
	private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;
	
	private static final String AUTH_DOMAIN_URL = "https://%s.auth.%s.amazoncognito.com/oauth2/token";

	private String accessToken;
	
	@Override
	public String getAccessToken() {
		if(accessToken == null || this.isAccessTokenExpired()) {
			this.accessToken = this.getNewAccessToken();
		}
		return this.accessToken;
	}

	private String getNewAccessToken() {
		return WebClient.builder()
				.baseUrl(getAuthDomainUrl())
				.build()
				.post()
				.uri(uriBuilder -> uriBuilder
						.queryParam("grant_type", "client_credentials")
						.queryParam("client_id", cognitoConfig.getClientId())
						.queryParam("scope", applicationScopes)
						.build()
						)
				.header(JwtConstants.AUTHORIZATION_HEADER, JwtConstants.BASIC_TOKEN_TYPE + getAuthorizationHeader())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.retrieve()
				.bodyToMono(ClientAccessToken.class)
				.block()
				.getAccess_token();
	}

	private boolean isAccessTokenExpired() {
		try {
			this.configurableJWTProcessor.process(this.accessToken, null);
		} catch (ParseException | BadJOSEException | JOSEException e) {
			if(this.tokenExpired(e)) {
				return true;
			} else {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private boolean tokenExpired(Exception e) {
		return e.getMessage().equals(JwtConstants.EXPIRED_JWT);
	}

	private String getAuthDomainUrl() {
		return String.format(AUTH_DOMAIN_URL, authDomain, cognitoConfig.getRegion());
	}
	
	private String getAuthorizationHeader() {
		String clientIdClientSecretString = cognitoConfig.getClientId() + ":" + cognitoConfig.getClientSecret();
		return Base64.getEncoder().encodeToString(clientIdClientSecretString.getBytes());
	}
}
