package com.oracle.controller;

import com.oracle.entity.Policy;
import com.oracle.entity.PolicyStatus;
import com.oracle.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {
    private final PolicyService service;

    public PolicyController(PolicyService service) {
        this.service = service;
    }

    @GetMapping
    public List<Policy> list(@RequestHeader("X-USER-ID") String customerId,
                             @RequestParam(value = "status", required = false) PolicyStatus status) {
        return service.list(customerId, status);
    }

    @GetMapping("/{id}")
    public Policy get(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        return service.get(id);
    }
}
