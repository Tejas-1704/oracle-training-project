package com.oracle.controller;

import com.oracle.dto.ClaimAssessRequest;
import com.oracle.dto.ClaimRequest;
import com.oracle.dto.ClaimUpdateRequest;
import com.oracle.entity.Claim;
import com.oracle.entity.ClaimStatus;
import com.oracle.service.ClaimService;
import jakarta.validation.Valid;
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
    public Claim file(@RequestHeader("X-USER-ID") String userId, @Valid @RequestBody ClaimRequest request) {
        return service.fileClaim(request.getPolicyId(), request.getLossDate(), request.getDescription());
    }

    @GetMapping
    public List<Claim> list(@RequestHeader("X-USER-ID") String userId,
                             @RequestParam(value = "policy_id", required = false) String policyId,
                             @RequestParam(value = "status", required = false) ClaimStatus status) {
        return service.list(policyId, status);
    }

    @GetMapping("/{id}")
    public Claim get(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Claim update(@RequestHeader("X-USER-ID") String userId, @PathVariable String id,
                        @Valid @RequestBody ClaimUpdateRequest request) {
        return service.update(id, request.getDescription(), request.getLossDate());
    }

    @PostMapping("/{id}/assess")
    public Claim assess(@RequestHeader("X-USER-ID") String userId, @PathVariable String id,
                        @Valid @RequestBody ClaimAssessRequest request) {
        return service.assess(id, request.getDecision(), request.getApprovedAmount(), request.getReason());
    }

    @PostMapping("/{id}/close")
    public Claim close(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        return service.close(id);
    }
}
