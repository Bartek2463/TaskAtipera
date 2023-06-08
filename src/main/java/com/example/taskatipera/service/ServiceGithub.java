package com.example.taskatipera.service;

import com.example.taskatipera.model.Branch;
import com.example.taskatipera.model.RepositoryGitHub;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class ServiceGithub {
    private final ClientGitHub clientGitHub;

    public ServiceGithub(ClientGitHub clientGitHub) {
        this.clientGitHub = clientGitHub;
    }

     public Optional<Set<RepositoryGitHub>> findOther(String userName){
         Set<RepositoryGitHub> githubRepos = clientGitHub.getRepoGit(userName).stream()
                 .filter(r -> r.forkies()!=true)
                 .map(r -> {
                     String[] ownerAndReponame = r.name().split("/");
                     List<Branch> branches  = clientGitHub.getBranch(ownerAndReponame[0], ownerAndReponame[1]);

                     return new RepositoryGitHub(ownerAndReponame[1], ownerAndReponame[0], branches);
                 })
                 .collect(Collectors.toSet());
         return Optional.ofNullable(githubRepos);
     }
}
