package com.oracle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClaimUpdateRequest {
    @NotNull
    private LocalDate lossDate;
    @NotBlank
    private String description;
}
