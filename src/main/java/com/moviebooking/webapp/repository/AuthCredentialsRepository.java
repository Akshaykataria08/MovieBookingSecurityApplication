package com.moviebooking.webapp.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.moviebooking.webapp.domain.OidcTokens;

@EnableScan
public interface AuthCredentialsRepository extends CrudRepository<OidcTokens, String> {

}
