package com.oracle.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentCreateRequest {
    @NotBlank
    private String targetType;
    @NotBlank
    private String targetId;
    @NotNull
    @Positive
    private Long amount;
    @NotBlank
    private String method;
}
