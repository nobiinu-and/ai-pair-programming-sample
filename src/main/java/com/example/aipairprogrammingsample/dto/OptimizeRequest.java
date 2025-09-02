package com.example.aipairprogrammingsample.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 最適化リクエストDTO
 */
public class OptimizeRequest {

    @NotEmpty(message = "requests cannot be empty")
    @Valid
    private List<MetalRequest> requests;

    // デフォルトコンストラクタ
    public OptimizeRequest() {}

    // コンストラクタ
    public OptimizeRequest(List<MetalRequest> requests) {
        this.requests = requests;
    }

    // Getters and Setters
    public List<MetalRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<MetalRequest> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "OptimizeRequest{" +
                "requests=" + requests +
                '}';
    }
}
