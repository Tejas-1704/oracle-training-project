package com.oracle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_code", columnList = "code", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(unique = true)
    private String code;

    private String description;

    private int baseRatePer1000;
    private int minSumAssured;
    private int maxSumAssured;
    private int minTermMonths;
    private int maxTermMonths;
    @Column(name="active")
    private boolean isActive;
    
    @Version
    private Long version;

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
