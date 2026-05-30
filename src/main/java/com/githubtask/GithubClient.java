package com.githubtask;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GithubClient {

    private final RestClient restClient;

    GithubClient(RestClient restClient) {
        this.restClient = restClient;
    }

    List<GithubRepositoryResponse> getRepositories(String username) {
        GithubRepositoryResponse[] response = restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(GithubRepositoryResponse[].class);

        return response == null ? List.of() : List.of(response);
    }

    List<GithubBranchResponse> getBranches(String owner, String repositoryName) {
        GithubBranchResponse[] response = restClient.get()
                .uri("/repos/{owner}/{repositoryName}/branches", owner, repositoryName)
                .retrieve()
                .body(GithubBranchResponse[].class);

        return response == null ? List.of() : List.of(response);
    }
}