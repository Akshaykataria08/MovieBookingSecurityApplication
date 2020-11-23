package com.moviebooking.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class CognitoConfig {
	
	@Value("${aws.cognito.clientId}")
	private String clientId;
	
	@Value("${aws.cognito.client-secret}")
	private String clientSecret;
	
	@Value("${aws.cognito.region}")
	private String region;
	
	@Value("${aws.cognito.userPoolId}")
	private String userPoolId;
	
	@Bean
	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		return AWSCognitoIdentityProviderClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.getEndpoint(), region))
				.build();
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
	public String getEndpoint() {
		return String.format("cognito-idp.%s.amazonaws.com", region);
	}

	public String getRegion() {
		return region;
	}

	public String getUserPoolId() {
		return userPoolId;
	}
	
	public String getAdminGroup() {
		return "ADMIN";
	}
	
	public String getUserGroup() {
		return "USER";
	}
}
