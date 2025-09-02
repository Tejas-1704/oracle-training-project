package com.oracle.document;

import com.oracle.document.dto.DocumentCreateRequest;
import com.oracle.document.dto.DocumentResponse;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository repository;
    private final GridFsTemplate gridFsTemplate;

    public DocumentService(DocumentRepository repository, GridFsTemplate gridFsTemplate) {
        this.repository = repository;
        this.gridFsTemplate = gridFsTemplate;
    }

    public DocumentResponse save(DocumentCreateRequest req, MultipartFile file) {
        try {
            String gridId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType()).toString();
            DocumentMeta meta = DocumentMeta.builder()
                    .id(UUID.randomUUID().toString())
                    .ownerId(req.getOwnerId())
                    .ownerType(req.getOwnerType())
                    .label(req.getLabel())
                    .fileName(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .sizeBytes(file.getSize())
                    .gridFsId(gridId)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
            repository.save(meta);
            return toDto(meta);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to store file", e);
        }
    }

    public List<DocumentResponse> list(String ownerId, String ownerType) {
        return repository.findByOwnerIdAndOwnerType(ownerId, ownerType).stream().map(this::toDto).collect(Collectors.toList());
    }

    public DocumentResponse get(String id) {
        return repository.findById(id).map(this::toDto).orElseThrow();
    }

    public void delete(String id) {
        DocumentMeta meta = repository.findById(id).orElseThrow();
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(meta.getGridFsId())));
        repository.delete(meta);
    }

    private DocumentResponse toDto(DocumentMeta meta) {
        return DocumentResponse.builder()
                .id(meta.getId())
                .ownerId(meta.getOwnerId())
                .ownerType(meta.getOwnerType())
                .label(meta.getLabel())
                .fileName(meta.getFileName())
                .mimeType(meta.getMimeType())
                .sizeBytes(meta.getSizeBytes())
                .createdAt(meta.getCreatedAt())
                .build();
    }
}
