package com.oracle.dto;

import lombok.Data;

@Data
public class ClaimAssessRequest {
    private String decision; // APPROVE or REJECT
    private Integer approvedAmount;
    private String reason;
}
