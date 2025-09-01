package com.oracle.document;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<DocumentMeta, String> {
    List<DocumentMeta> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
}
