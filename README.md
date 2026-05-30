# GithubTask

A Spring Boot REST API that acts as a proxy to the [GitHub API v3](https://developer.github.com/v3), exposing non-fork repositories with their branches for a given GitHub user.

## Tech Stack

- Java 25
- Spring Boot 4.0.6
- Gradle with Kotlin DSL

## Getting Started

### Prerequisites

- JDK 25

### Run the application

```bash
./gradlew bootRun
```

The application starts on `http://localhost:8080`.

## API

### Get repositories for a user

Returns all non-fork repositories for the given GitHub username, including branch names and their last commit SHA.

```
GET /api/github/{username}/repositories
```

**Path variable**

| Name       | Description         |
|------------|---------------------|
| `username` | GitHub username     |

**Success response — 200 OK**

```json
[
  {
    "repositoryName": "repo",
    "ownerLogin": "test-user",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123"
      }
    ]
  }
]
```

**User not found — 404 Not Found**

```json
{
  "status": 404,
  "message": "GitHub user: qwe not found"
}
```

## Running Tests

Integration tests use [WireMock](https://wiremock.org/) to mock the GitHub API — no real network calls are made.

```bash
./gradlew test
```