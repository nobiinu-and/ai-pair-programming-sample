package com.example.aipairprogrammingsample.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 金属リクエストDTO
 */
public class MetalRequest {

    @NotBlank(message = "metalCode is required")
    private String metalCode;

    @Min(value = 1, message = "requiredQuantity must be >= 1")
    private Integer requiredQuantity;

    // デフォルトコンストラクタ
    public MetalRequest() {}

    // コンストラクタ
    public MetalRequest(String metalCode, Integer requiredQuantity) {
        this.metalCode = metalCode;
        this.requiredQuantity = requiredQuantity;
    }

    // Getters and Setters
    public String getMetalCode() {
        return metalCode;
    }

    public void setMetalCode(String metalCode) {
        this.metalCode = metalCode;
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public String toString() {
        return "MetalRequest{" +
                "metalCode='" + metalCode + '\'' +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }
}
