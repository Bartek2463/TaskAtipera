package com.example.taskatipera.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record GitRepositoryDTO(boolean forkies, @JsonProperty("full_name") String name, @JsonProperty ("branches_url")String url) {
}
