package com.moviebooking.webapp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.moviebooking.webapp.responsedto.ExceptionResponse;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.moviebooking.webapp.service.ClientAccessTokenService;

import reactor.core.publisher.Mono;

@Component
public class ResponseUtility {

	@Autowired
	private ClientAccessTokenService clientAccessTokenService;

	public <T extends Response> Mono<ResponseEntity<? extends Response>> sendGetRequest(String baseUrl, String path,
			Class<T> responseClass) {
		return getWebclient(baseUrl).get().uri(path)
				.exchange().flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response, E> Mono<ResponseEntity<? extends Response>> sendPostRequest(String baseUrl, String path, E requestBody,
			Class<E> requestBodyClass, Class<T> responseClass) {
		return getWebclient(baseUrl).post().uri(path)
				.body(BodyInserters.fromPublisher(Mono.just(requestBody), requestBodyClass))
				.exchange().flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response> Mono<ResponseEntity<? extends Response>> sendPostRequest(String baseUrl, String path,
			Class<T> responseClass) {
		return getWebclient(baseUrl).post().uri(path)
				.exchange().flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response, E> Mono<ResponseEntity<? extends Response>> sendPutRequest(String baseUrl, String path, E requestBody,
			Class<E> requestBodyClass, Class<T> responseClass) {
		return getWebclient(baseUrl).put().uri(path)
				.body(BodyInserters.fromPublisher(Mono.just(requestBody), requestBodyClass))
				.exchange().flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response> Mono<ResponseEntity<? extends Response>> sendPutRequest(String baseUrl, String path,
			Class<T> responseClass) {
		return getWebclient(baseUrl).put().uri(path)
				.exchange().flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response> Mono<ResponseEntity<? extends Response>> sendDeleteRequest(String baseUrl, String path,
			Class<T> responseClass) {
		return getWebclient(baseUrl).delete().uri(path).exchange().flatMap(clientResponse -> {
			return this.getResponse(responseClass, clientResponse);
		});
	}

	private <T extends Response> Mono<? extends ResponseEntity<? extends Response>> getResponse(Class<T> responseClass,
			ClientResponse clientResponse) {
		if (clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()) {
			return clientResponse.toEntity(ExceptionResponse.class);
		}
		return clientResponse.toEntity(responseClass);
	}

	private WebClient getWebclient(String baseUrl) {
		return WebClient.builder().baseUrl(baseUrl).defaultHeaders(httpHeaders -> {
			httpHeaders.add(JwtConstants.AUTHORIZATION_HEADER,
					JwtConstants.BEARER_TOKEN_TYPE + clientAccessTokenService.getAccessToken());
			httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		}).build();
	}
}
