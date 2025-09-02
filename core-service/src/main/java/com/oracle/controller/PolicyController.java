package com.oracle.controller;

import com.oracle.entity.Policy;
import com.oracle.entity.PolicyStatus;
import com.oracle.service.JwtService;
import com.oracle.service.PolicyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {
    private final PolicyService service;
    private final JwtService jwtService;

    public PolicyController(PolicyService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<Policy> list(HttpServletRequest request,
                             @RequestParam(value = "status", required = false) PolicyStatus status) {
        String customerId = currentUserId(request);
        return service.list(customerId, status);
    }

    @GetMapping("/{id}")
    public Policy get(HttpServletRequest request, @PathVariable String id) {
        currentUserId(request);
        return service.get(id);
    }

    private String currentUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : "";
        return jwtService.extractUserId(token);
    }
}
