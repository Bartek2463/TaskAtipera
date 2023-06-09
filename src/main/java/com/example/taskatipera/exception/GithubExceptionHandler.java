package com.example.taskatipera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GithubExceptionHandler {

    @ExceptionHandler(value = GithubRepoException.class)
    public ResponseEntity<ErrorInfo> handleException(GithubRepoException e){
        HttpStatus httpStatus = switch (e.getGithubRepoError()){
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.NOT_FOUND;
        };
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getGithubRepoError().getErrorInfo(), String.valueOf(HttpStatus.NOT_FOUND.value())));
    }
}
