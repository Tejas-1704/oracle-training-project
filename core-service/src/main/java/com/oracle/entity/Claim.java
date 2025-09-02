package com.oracle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank
    @Column(unique = true)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    @NotNull
    private Policy policy;

    @NotNull
    private LocalDate lossDate;
    @NotBlank
    private String description;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.OPEN;

    @Positive
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
