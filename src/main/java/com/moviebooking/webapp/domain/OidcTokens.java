package com.moviebooking.webapp.domain;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamoDBTable(tableName = "Credentials")
public class OidcTokens {

	@DynamoDBHashKey
	private String username;
	
	@DynamoDBAttribute
	private String accessToken;
	
	@DynamoDBAttribute
	private String idToken;
	
	@DynamoDBAttribute
	private String refreshToken;
	
	@DynamoDBAttribute
	private Date expiryDate;
}
