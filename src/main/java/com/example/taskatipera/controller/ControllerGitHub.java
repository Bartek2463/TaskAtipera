package com.example.taskatipera.controller;

import com.example.taskatipera.model.RepositoryGitHub;
import com.example.taskatipera.service.ServiceGithub;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/repos")
public class ControllerGitHub {
    private final ServiceGithub serviceGithub;

    public ControllerGitHub(ServiceGithub serviceGithub) {
        this.serviceGithub = serviceGithub;
    }

    @GetMapping("/{userName}")
    public List<RepositoryGitHub> findAllRepository(@PathVariable("userName")String userName){
        return serviceGithub.findOther(userName)
                .orElseThrow(()->new RuntimeException());
    }
}
