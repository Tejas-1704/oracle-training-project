package com.oracle.controller;

import com.oracle.dto.ClaimAssessRequest;
import com.oracle.dto.ClaimRequest;
import com.oracle.dto.ClaimUpdateRequest;
import com.oracle.entity.Claim;
import com.oracle.entity.ClaimStatus;
import com.oracle.service.ClaimService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimController {
    private final ClaimService service;

    public ClaimController(ClaimService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Claim file(@RequestBody ClaimRequest request) {
        return service.fileClaim(request.getPolicyId(), request.getLossDate(), request.getDescription());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Claim> list(@RequestParam(value = "policy_id", required = false) String policyId,
                             @RequestParam(value = "status", required = false) ClaimStatus status) {
        return service.list(policyId, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Claim get(@PathVariable String id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Claim update(@PathVariable String id, @RequestBody ClaimUpdateRequest request) {
        return service.update(id, request.getDescription(), request.getLossDate());
    }

    @PostMapping("/{id}/assess")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Claim assess(@PathVariable String id, @RequestBody ClaimAssessRequest request) {
        return service.assess(id, request.getDecision(), request.getApprovedAmount(), request.getReason());
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Claim close(@PathVariable String id) {
        return service.close(id);
    }
}
