package com.example.aipairprogrammingsample.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * 最適化リクエストDTO
 */
public class OptimizeRequest {

    @NotEmpty(message = "requests cannot be empty")
    @Valid
    private List<MetalRequest> requests;

    public OptimizeRequest() {
        this.requests = new ArrayList<>();
    }

    public OptimizeRequest(List<MetalRequest> requests) {
        if (requests == null) {
            this.requests = new ArrayList<>();
        } else {
            this.requests = new ArrayList<>(requests);
        }
    }

    public List<MetalRequest> getRequests() {
        return new ArrayList<>(this.requests);
    }

    public void setRequests(List<MetalRequest> requests) {
        this.requests = new ArrayList<>(requests);
    }

}
