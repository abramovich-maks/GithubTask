package com.githubtask;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubService {

    private final GithubClient githubClient;

    GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    List<Repository> getRepositories(String username) {
        return githubClient.getRepositories(username)
                .stream()
                .filter(repository -> !repository.fork())
                .map(this::mapRepository)
                .toList();
    }

    private Repository mapRepository(GithubRepositoryResponse repositoryResponse) {
        String owner = repositoryResponse.owner().login();
        String repositoryName = repositoryResponse.name();

        List<Branch> branches = githubClient.getBranches(owner, repositoryName)
                .stream()
                .map(branch -> new Branch(
                        branch.name(),
                        branch.commit().sha()
                ))
                .toList();

        return new Repository(repositoryName, owner, branches);
    }
}
