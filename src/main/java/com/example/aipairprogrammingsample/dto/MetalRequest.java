package com.example.aipairprogrammingsample.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 金属リクエストDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetalRequest {

    @NotBlank(message = "metalCode is required")
    private String metalCode;

    @Min(value = 1, message = "requiredQuantity must be >= 1")
    private Integer requiredQuantity;
}
