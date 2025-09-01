package com.oracle.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClaimRequest {
    private String policyId;
    private LocalDate lossDate;
    private String description;
}
