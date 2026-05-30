package com.githubtask;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@WireMockTest(httpPort = 8089)
class GithubTaskApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void overrideGithubBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("github.base-url", () -> "http://localhost:8089");
    }

    @Test
    void shouldReturnRepositoriesWithBranchesForExistingUser() {
        stubFor(get(urlEqualTo("/users/test-user/repos")).willReturn(okJson("""
                [
                  {"name": "repo", "fork": false, "owner": {"login": "test-user"}},
                  {"name": "fork",  "fork": true,  "owner": {"login": "test-user"}}
                ]
                """)));

        stubFor(get(urlEqualTo("/repos/test-user/repo/branches")).willReturn(okJson("""
                [
                  {"name": "main", "commit": {"sha": "abc123"}},
                  {"name": "dev",  "commit": {"sha": "def456"}}
                ]
                """)));

        ResponseEntity<RepositoryResponseDto[]> response = restTemplate.getForEntity("/api/github/test-user/repositories", RepositoryResponseDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        RepositoryResponseDto repo = response.getBody()[0];
        assertThat(repo.repositoryName()).isEqualTo("repo");
        assertThat(repo.ownerLogin()).isEqualTo("test-user");
        assertThat(repo.branches()).extracting("name").containsExactlyInAnyOrder("main", "dev");
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() {
        stubFor(get(urlEqualTo("/users/qwe/repos")).willReturn(aResponse().withStatus(404)));

        ResponseEntity<ErrorResponseDto> response = restTemplate.getForEntity("/api/github/qwe/repositories", ErrorResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).contains("qwe");
    }
}