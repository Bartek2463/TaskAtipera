package com.example.taskatipera.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {

        if(HttpStatus.NOT_FOUND.value() == response.status()) return new GithubRepoException(GithubRepoError.USER_NOT_FOUND);
        else return new Exception("Something went wrong.");
    }
}
