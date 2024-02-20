package com.example.taskatipera;

import com.example.taskatipera.dto.GitRepositoryDTO;
import com.example.taskatipera.model.Branch;
import com.example.taskatipera.model.GitCommit;
import com.example.taskatipera.model.Owner;
import com.example.taskatipera.model.RepositoryGitHub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
		final String username = "notExistingUser";
		final String gitHubRepositoryEndpoint = "/users/" + username + "/repos";

		wireMockExtension.stubFor(get(gitHubRepositoryEndpoint)
				.willReturn(notFound()
						.withHeader("Content-Type", "application/json")));


		webTestClient
				.get()
				.uri("/repos"+username)
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(NOT_FOUND);
	}

	@Test
	public void when_User_isExist(){
		final String username = "Userpast";
		final String repo = "repoPast";
		final String gitHubRepoEndpoint = "/users/" + username + "/repos";
		final String gitHubBranchesEndpoint = "/repos/"+username+"/"+repo+"/branches";

		final GitCommit gitCommit = new GitCommit("4e825fab11f913ff31e209896776af2b05a2b3da");
		final Branch branch = new Branch("branchPast", gitCommit);
		final List<Branch> branchList = new ArrayList<>();
		branchList.add(branch);
		final Owner owner = new Owner(username);
		final List<RepositoryGitHub> repositoryGitHubs = new ArrayList<>();
		repositoryGitHubs.add(new RepositoryGitHub(repo,owner.login(),branchList));
		final List<GitRepositoryDTO> reposListDTO = new ArrayList<>();
		reposListDTO.add(new GitRepositoryDTO(true,username,gitHubBranchesEndpoint));



		final ObjectMapper objectMapper = new ObjectMapper();

		wireMockExtension.stubFor(get(gitHubRepoEndpoint)
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(objectMapper.writeValueAsString(repositoryGitHubs))));

		wireMockExtension.stubFor(get(gitHubBranchesEndpoint)
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(objectMapper.writeValueAsString(branchList))));

		webTestClient
				.get()
				.uri("/api/" + username)
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(OK)
				.expectBody(ReposListDTO.class)
				.isEqualTo(reposListDTO);

		verify(getRequestedFor(urlEqualTo(gitHubReposEndpoint)));
		verify(getRequestedFor(urlEqualTo(gitHubBranchesEndpoint)));

			}


}
