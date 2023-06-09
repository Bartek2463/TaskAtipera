package com.example.taskatipera.service;

import com.example.taskatipera.dto.GitRepositoryDTO;
import com.example.taskatipera.model.Branch;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@FeignClient(name = "github-api",url = "https://api.github.com")
@Headers("Accept: application/json")
public interface ClientGitHub {

    @GetMapping("/users/{userName}/repos")
    List<GitRepositoryDTO> getRepoGit(@PathVariable("userName")String userName);

    @GetMapping("/repos/{userName}/{repoName}/branches")
    List<Branch> getBranch(@PathVariable("userName")String userName,@PathVariable("repoName")String repositoryName);
}
