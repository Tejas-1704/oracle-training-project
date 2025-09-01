package com.oracle.controller;

import com.oracle.dto.QuoteRequest;
import com.oracle.dto.QuoteUpdateRequest;
import com.oracle.entity.Policy;
import com.oracle.entity.Quote;
import com.oracle.entity.QuoteStatus;
import com.oracle.service.QuoteService;
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
    public Quote create(@RequestBody QuoteRequest request) {
        return service.create(request.getCustomerId(), request.getProductId(), request.getSumAssured(), request.getTermMonths());
    }

    @GetMapping
    public List<Quote> list(@RequestParam(value = "customer_id", required = false) String customerId,
                             @RequestParam(value = "status", required = false) QuoteStatus status) {
        return service.list(customerId, status);
    }

    @GetMapping("/{id}")
    public Quote get(@PathVariable String id) {
        return service.get(id);
    }

    // need to use request header instead of PathVariable
    @PatchMapping("/{id}")
    public Quote update(@PathVariable String id, @RequestBody QuoteUpdateRequest req) {
        return service.update(id, req.getSumAssured(), req.getTermMonths());
    }

    @PostMapping("/{id}/price")
    public Quote price(@PathVariable String id) {
        return service.price(id);
    }

    @PostMapping("/{id}/confirm")
    public Policy confirm(@PathVariable String id) {
        return service.confirm(id);
    }
}
