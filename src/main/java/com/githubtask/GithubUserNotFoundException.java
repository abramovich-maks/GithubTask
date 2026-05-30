package com.githubtask;

class GithubUserNotFoundException extends RuntimeException {

    GithubUserNotFoundException(String username) {
        super("GitHub user: " + username + " not found");
    }
}