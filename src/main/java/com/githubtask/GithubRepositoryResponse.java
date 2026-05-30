package com.githubtask;

record GithubRepositoryResponse(
        String name,
        Owner owner,
        boolean fork
) {
}