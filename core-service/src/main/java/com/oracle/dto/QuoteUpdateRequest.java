package com.oracle.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuoteUpdateRequest {
    @Positive
    private Integer sumAssured;
    @Positive
    private Integer termMonths;
}
