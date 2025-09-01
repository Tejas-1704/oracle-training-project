package com.oracle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "claims", indexes = {
        @Index(name = "idx_claims_policy", columnList = "policy_id"),
        @Index(name = "idx_claims_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    private LocalDate lossDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.OPEN;

    private Integer approvedAmount;
    private String decisionReason;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
