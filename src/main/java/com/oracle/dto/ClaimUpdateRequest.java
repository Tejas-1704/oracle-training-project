package com.oracle.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClaimUpdateRequest {
    private LocalDate lossDate;
    private String description;
}
