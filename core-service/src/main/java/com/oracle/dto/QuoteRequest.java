package com.oracle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuoteRequest {
    @NotBlank
    private String productId;
    @Positive
    private int sumAssured;
    @Positive
    private int termMonths;
}
