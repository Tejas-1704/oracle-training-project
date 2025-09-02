package com.oracle.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PaymentResponse {
    private String id;
    private String targetType;
    private String targetId;
    private long amount;
    private String method;
    private String status;
    private String gatewayRef;
    private Instant createdAt;
}
