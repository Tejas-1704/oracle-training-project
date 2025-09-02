package com.oracle.controller;

import com.oracle.dto.QuoteRequest;
import com.oracle.dto.QuoteUpdateRequest;
import com.oracle.entity.Policy;
import com.oracle.entity.Quote;
import com.oracle.entity.QuoteStatus;
import com.oracle.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {
    private final QuoteService service;

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    @PostMapping
    public Quote create(@RequestHeader("X-USER-ID") String customerId, @Valid @RequestBody QuoteRequest request) {
        return service.create(customerId, request.getProductId(), request.getSumAssured(), request.getTermMonths());
    }

    @GetMapping
    public List<Quote> list(@RequestHeader("X-USER-ID") String customerId,
                             @RequestParam(value = "status", required = false) QuoteStatus status) {
        return service.list(customerId, status);
    }

    @GetMapping("/{id}")
    public Quote get(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Quote update(@RequestHeader("X-USER-ID") String userId, @PathVariable String id,
                        @Valid @RequestBody QuoteUpdateRequest req) {
        return service.update(id, req.getSumAssured(), req.getTermMonths());
    }

    @PostMapping("/{id}/price")
    public Quote price(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        return service.price(id);
    }

    @PostMapping("/{id}/confirm")
    public Policy confirm(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        return service.confirm(id);
    }
}
