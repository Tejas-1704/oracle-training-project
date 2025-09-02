package com.oracle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "policies", indexes = {
        @Index(name = "idx_policies_customer", columnList = "customer_id"),
        @Index(name = "idx_policies_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(unique = true)
    private String policyNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    @Positive
    private int sumAssured;
    @Positive
    private int termMonths;
    @Positive
    private int premium;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.ACTIVE;

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
