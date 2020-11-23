package com.moviebooking.webapp.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.moviebooking.webapp.security.jwt.AwsCognitoIdTokenProcessor;

@Component
public class AwsCognitoJwtAuthFilter extends OncePerRequestFilter {

	private AwsCognitoIdTokenProcessor cognitoIdTokenProcessor;

	public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor cognitoIdTokenProcessor) {
        this.cognitoIdTokenProcessor = cognitoIdTokenProcessor;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		Authentication authentication;
		try {
			authentication = this.cognitoIdTokenProcessor.authenticate(request, response);
			if (authentication != null) {
				authentication.setAuthenticated(true);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}
