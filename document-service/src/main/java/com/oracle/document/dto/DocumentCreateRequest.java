package com.oracle.document.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentCreateRequest {
    @NotBlank
    private String ownerId;
    @NotBlank
    private String ownerType;
    @NotBlank
    private String label;
}
