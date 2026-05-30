package com.githubtask;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice()
class GithubErrorHandler {

    @ExceptionHandler(GithubUserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponseDto handleException(GithubUserNotFoundException exception) {
        return new ErrorResponseDto(404, exception.getMessage());
    }
}