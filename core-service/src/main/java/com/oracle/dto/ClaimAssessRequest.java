package com.oracle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ClaimAssessRequest {
    @NotBlank
    private String decision; // APPROVE or REJECT
    @Positive
    private Integer approvedAmount;
    @NotNull
    private String reason;
}
