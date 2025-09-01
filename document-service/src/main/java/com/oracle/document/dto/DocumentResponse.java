package com.oracle.document.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DocumentResponse {
    private String id;
    private String ownerId;
    private String ownerType;
    private String label;
    private String fileName;
    private String mimeType;
    private long sizeBytes;
    private Instant createdAt;
}
