package com.moviebooking.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.moviebooking.webapp.security.filter.AwsCognitoJwtAuthFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().cacheControl();
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			
			.antMatchers(HttpMethod.POST, "/v1/location/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/v1/location/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/v1/location/**").hasRole("ADMIN")
			
			.antMatchers(HttpMethod.POST, "/v1/movie/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/v1/movie/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/v1/movie/**").hasRole("ADMIN")
			
			.antMatchers(HttpMethod.POST, "/v1/show/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/v1/show/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/v1/show/**").hasRole("ADMIN")
			
			.antMatchers(HttpMethod.POST, "/v1/theatre/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/v1/theatre/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/v1/theatre/**").hasRole("ADMIN")
			
			.antMatchers("/v1/booking/**", "/v1/changePassword", "/v1/logout").authenticated()
			
			.anyRequest().permitAll()
			.and()
			.addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
