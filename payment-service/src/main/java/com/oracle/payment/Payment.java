package com.oracle.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String id;
    private String targetType;
    private String targetId;
    private long amount;
    private String method;
    private String status;
    private String gatewayRef;
    private Instant createdAt;
    private Instant updatedAt;
}
