package com.oracle.document;

import com.oracle.document.dto.DocumentCreateRequest;
import com.oracle.document.dto.DocumentResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentResponse upload(@RequestPart("meta") @Valid DocumentCreateRequest request,
                                   @RequestPart("file") MultipartFile file) {
        return service.save(request, file);
    }

    @GetMapping
    public List<DocumentResponse> list(@RequestParam String ownerId, @RequestParam String ownerType) {
        return service.list(ownerId, ownerType);
    }

    @GetMapping("/{id}")
    public DocumentResponse get(@PathVariable String id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable String id) {
        service.delete(id);
        return Map.of("deleted", true);
    }
}
