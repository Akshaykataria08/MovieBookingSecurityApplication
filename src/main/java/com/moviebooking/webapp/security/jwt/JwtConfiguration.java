package com.moviebooking.webapp.security.jwt;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

@Configuration
public class JwtConfiguration {

	@Autowired
	private JwtConstants jwtConstants;
	
	@Bean
	public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor() throws MalformedURLException {
		ResourceRetriever resourceRetriever = 
				new DefaultResourceRetriever(2000, 2000);
						URL jwkSetURL= new URL(jwtConstants.getWellKnownJwkUrl());
				//Creates the JSON Web Key (JWK)
				JWKSource<SecurityContext> keySource= new RemoteJWKSet<>(jwkSetURL, resourceRetriever);
				ConfigurableJWTProcessor<SecurityContext> jwtProcessor= new DefaultJWTProcessor<>();
				//RSASSA-PKCS-v1_5 using SHA-256 hash algorithm
				JWSKeySelector<SecurityContext> keySelector= new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
				jwtProcessor.setJWSKeySelector(keySelector);
				return jwtProcessor;
	}
}
