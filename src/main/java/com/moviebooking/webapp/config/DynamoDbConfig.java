package com.moviebooking.webapp.config;

import javax.annotation.PostConstruct;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.moviebooking.webapp.domain.OidcTokens;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableDynamoDBRepositories(basePackages = "com.moviebooking.webapp.repository")
public class DynamoDbConfig {

	@Value("${dynamodb.end-point.url}")
	private String awsDynamoDBEndPoint;

	@Value("${dynamodb.region}")
	private String region;
    
	@Value("${dynamo-ddl: update}")
	private String dynamoDbDDL;
	
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, region))
				.build();
	}

	@PostConstruct
	private void setupTables() {
		AmazonDynamoDB amazonDynamoDB = amazonDynamoDB();
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

		if(dynamoDbDDL.equalsIgnoreCase("create")) {
			TableUtils.deleteTableIfExists(amazonDynamoDB, mapper.generateDeleteTableRequest(OidcTokens.class));
			log.info("Dropped All tables");
		}
		
		CreateTableRequest authCredentials = mapper.generateCreateTableRequest(OidcTokens.class);
		authCredentials.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		TableUtils.createTableIfNotExists(amazonDynamoDB, authCredentials);
		log.info("Auth Table Created");
	}
}
