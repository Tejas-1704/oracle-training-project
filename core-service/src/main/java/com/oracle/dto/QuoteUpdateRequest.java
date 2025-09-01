package com.oracle.dto;

import lombok.Data;

@Data
public class QuoteUpdateRequest {
    private Integer sumAssured;
    private Integer termMonths;
}
