package com.example.taskatipera.exception;

public enum GithubRepoError {
    USER_NOT_FOUND("There is not such a user in github. Check if username is not misspelled");

    private String errorInfo;

    GithubRepoError(String errorInfo){this.errorInfo = errorInfo; }

    public String getErrorInfo(){return errorInfo; }
}
