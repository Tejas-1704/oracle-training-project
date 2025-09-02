package com.oracle.controller;

import com.oracle.dto.ClaimAssessRequest;
import com.oracle.dto.ClaimRequest;
import com.oracle.dto.ClaimUpdateRequest;
import com.oracle.entity.Claim;
import com.oracle.entity.ClaimStatus;
import com.oracle.service.ClaimService;
import com.oracle.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimController {
    private final ClaimService service;
    private final JwtService jwtService;

    public ClaimController(ClaimService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping
    public Claim file(HttpServletRequest request, @Valid @RequestBody ClaimRequest rq) {
        currentUserId(request);
        return service.fileClaim(rq.getPolicyId(), rq.getLossDate(), rq.getDescription());
    }

    @GetMapping
    public List<Claim> list(HttpServletRequest request,
                             @RequestParam(value = "policy_id", required = false) String policyId,
                             @RequestParam(value = "status", required = false) ClaimStatus status) {
        currentUserId(request);
        return service.list(policyId, status);
    }

    @GetMapping("/{id}")
    public Claim get(HttpServletRequest request, @PathVariable String id) {
        currentUserId(request);
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Claim update(HttpServletRequest request, @PathVariable String id,
                        @Valid @RequestBody ClaimUpdateRequest rq) {
        currentUserId(request);
        return service.update(id, rq.getDescription(), rq.getLossDate());
    }

    @PostMapping("/{id}/assess")
    public Claim assess(HttpServletRequest request, @PathVariable String id,
                        @Valid @RequestBody ClaimAssessRequest rq) {
        currentUserId(request);
        return service.assess(id, rq.getDecision(), rq.getApprovedAmount(), rq.getReason());
    }

    @PostMapping("/{id}/close")
    public Claim close(HttpServletRequest request, @PathVariable String id) {
        currentUserId(request);
        return service.close(id);
    }

    private String currentUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : "";
        return jwtService.extractUserId(token);
    }
}
