package com.example.taskatipera;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TaskAtiperaApplicationTests {

	@RegisterExtension
	static WireMockExtension wireMockExtension =  WireMockExtension.newInstance()
			.options(wireMockConfig().dynamicPort().notifier(new ConsoleNotifier(true)))
			.build();


	@DynamicPropertySource
	static  void configureProperties(DynamicPropertyRegistry registry){
		registry.add("github-api",wireMockExtension::baseUrl);
	}

	@Autowired
	private WebTestClient webTestClient;

	@Test
	 public void userNotFound () {
		final String username = "notExisUser";
		final String gitHubRepositoryEndpoint = "/users/" + username + "/repos";

		wireMockExtension.stubFor(get(gitHubRepositoryEndpoint)
				.willReturn(notFound()
						.withHeader("Content-Type","application/json")));


		webTestClient
				.get()
				.uri("/repos"+username)
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(NOT_FOUND_404);
	}


}
