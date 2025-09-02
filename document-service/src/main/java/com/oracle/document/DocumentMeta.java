package com.oracle.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMeta {
    @Id
    private String id;
    private String ownerType;
    private String ownerId;
    private String label;
    private String fileName;
    private String mimeType;
    private long sizeBytes;
    private String gridFsId;
    private Instant createdAt;
    private Instant updatedAt;
}
