package com.oracle.dto;

import lombok.Data;

@Data
public class QuoteRequest {
    private String customerId;
    private String productId;
    private int sumAssured;
    private int termMonths;
}
