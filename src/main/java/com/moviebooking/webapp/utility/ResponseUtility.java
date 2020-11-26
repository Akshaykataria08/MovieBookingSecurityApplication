package com.moviebooking.webapp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
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
		return getWebclient(baseUrl).get().uri(path).exchange().flatMap(clientResponse -> {
			return this.getResponse(responseClass, clientResponse);
		});
	}

	public <T extends Response, E> Mono<ResponseEntity<? extends Response>> sendPostRequest(String baseUrl, String path,
			E requestBody, Class<E> requestBodyClass, Class<T> responseClass) {
		return getWebclient(baseUrl).post().uri(path)
				.body(BodyInserters.fromPublisher(Mono.just(requestBody), requestBodyClass)).exchange()
				.flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response> Mono<ResponseEntity<? extends Response>> sendPostRequest(String baseUrl, String path,
			Class<T> responseClass) {
		return getWebclient(baseUrl).post().uri(path).exchange().flatMap(clientResponse -> {
			return this.getResponse(responseClass, clientResponse);
		});
	}

	public <T extends Response, E> Mono<ResponseEntity<? extends Response>> sendPutRequest(String baseUrl, String path,
			E requestBody, Class<E> requestBodyClass, Class<T> responseClass) {
		return getWebclient(baseUrl).put().uri(path)
				.body(BodyInserters.fromPublisher(Mono.just(requestBody), requestBodyClass)).exchange()
				.flatMap(clientResponse -> {
					return this.getResponse(responseClass, clientResponse);
				});
	}

	public <T extends Response> Mono<ResponseEntity<? extends Response>> sendPutRequest(String baseUrl, String path,
			Class<T> responseClass) {
		return getWebclient(baseUrl).put().uri(path).exchange().flatMap(clientResponse -> {
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

	public <T extends Response> Mono<ResponseEntity<? extends Response>> uploadFile(String baseUrl, String path,
			MultipartFile file, Class<T> responseClass) {
		return this.getWebclient(baseUrl).post().uri(path).contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(this.fromFile(file))).exchange().flatMap(clientResponse -> {
					if (clientResponse.statusCode().is4xxClientError()
							|| clientResponse.statusCode().is5xxServerError()) {
						if (clientResponse.statusCode().value() == 400) {
							clientResponse.bodyToMono(responseClass).doOnNext(response -> System.out.println(response)).subscribe();
							return clientResponse.toEntity(responseClass);
						}
						return clientResponse.toEntity(ExceptionResponse.class);
					}
					return clientResponse.toEntity(responseClass);
				});
	}

	private MultiValueMap<String, HttpEntity<?>> fromFile(MultipartFile file) {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", file.getResource());
		return builder.build();
	}
}
