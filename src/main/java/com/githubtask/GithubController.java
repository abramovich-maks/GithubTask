package com.githubtask;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
class GithubController {

    private final GithubService githubService;

    GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}/repositories")
    ResponseEntity<List<RepositoryResponseDto>> getRepositories(@PathVariable String username) {
        List<Repository> repositories = githubService.getRepositories(username);

        List<RepositoryResponseDto> list = mapToDto(repositories);
        return ResponseEntity.ok(list);
    }

    private List<RepositoryResponseDto> mapToDto(final List<Repository> repositories) {
        return repositories.stream()
                .map(repo -> {
                    List<BranchResponseDto> mappedBranches = repo.branches().stream()
                            .map(branch -> new BranchResponseDto(branch.name(), branch.lastCommitSha()))
                            .toList();
                    return new RepositoryResponseDto(repo.name(), repo.ownerLogin(), mappedBranches);
                })
                .toList();
    }
}
