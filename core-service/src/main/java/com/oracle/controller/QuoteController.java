package com.oracle.controller;

import com.oracle.dto.QuoteRequest;
import com.oracle.dto.QuoteUpdateRequest;
import com.oracle.entity.Policy;
import com.oracle.entity.Quote;
import com.oracle.entity.QuoteStatus;
import com.oracle.service.JwtService;
import com.oracle.service.QuoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {
    private final QuoteService service;
    private final JwtService jwtService;

    public QuoteController(QuoteService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping
    public Quote create(HttpServletRequest request, @Valid @RequestBody QuoteRequest rq) {
        String customerId = currentUserId(request);
        return service.create(customerId, rq.getProductId(), rq.getSumAssured(), rq.getTermMonths());
    }

    @GetMapping
    public List<Quote> list(HttpServletRequest request,
                             @RequestParam(value = "status", required = false) QuoteStatus status) {
        String customerId = currentUserId(request);
        return service.list(customerId, status);
    }

    @GetMapping("/{id}")
    public Quote get(HttpServletRequest request, @PathVariable String id) {
        currentUserId(request); // ensure token valid
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Quote update(HttpServletRequest request, @PathVariable String id,
                        @Valid @RequestBody QuoteUpdateRequest req) {
        currentUserId(request);
        return service.update(id, req.getSumAssured(), req.getTermMonths());
    }

    @PostMapping("/{id}/price")
    public Quote price(HttpServletRequest request, @PathVariable String id) {
        currentUserId(request);
        return service.price(id);
    }

    @PostMapping("/{id}/confirm")
    public Policy confirm(HttpServletRequest request, @PathVariable String id) {
        currentUserId(request);
        return service.confirm(id);
    }

    private String currentUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : "";
        return jwtService.extractUserId(token);
    }
}
