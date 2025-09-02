package com.oracle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "quotes", indexes = {
        @Index(name = "idx_quotes_customer", columnList = "customer_id"),
        @Index(name = "idx_quotes_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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
    private Integer premiumCached;
    private String pricingSource;

    @Enumerated(EnumType.STRING)
    private QuoteStatus status = QuoteStatus.DRAFT;

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
