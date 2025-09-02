package com.oracle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClaimRequest {
    @NotBlank
    private String policyId;
    @NotNull
    private LocalDate lossDate;
    @NotBlank
    private String description;
}
