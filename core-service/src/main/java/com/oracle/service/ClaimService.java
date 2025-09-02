package com.oracle.service;

import com.oracle.entity.*;
import com.oracle.repository.*;
import com.oracle.kafka.NotificationPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.time.YearMonth;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final NotificationPublisher notificationPublisher;

    public ClaimService(ClaimRepository claimRepository, PolicyRepository policyRepository,
                        NotificationPublisher notificationPublisher) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public Claim fileClaim(String policyId, java.time.LocalDate lossDate, String description) {
        Policy policy = policyRepository.findById(policyId).orElseThrow();
        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new IllegalStateException("Policy not active");
        }
        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setLossDate(lossDate);
        claim.setDescription(description);
        claim.setClaimNumber(generateClaimNumber());
        claim.setStatus(ClaimStatus.OPEN);
        return claimRepository.save(claim);
    }

    public List<Claim> list(String policyId, ClaimStatus status) {
        if (policyId != null && status != null) {
            return claimRepository.findByPolicy_IdAndStatus(policyId, status);
        } else if (policyId != null) {
            return claimRepository.findByPolicy_Id(policyId);
        } else if (status != null) {
            return claimRepository.findByStatus(status);
        }
        return claimRepository.findAll();
    }

    public Claim get(String id) {
        return claimRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Claim update(String id, String description, java.time.LocalDate lossDate) {
        Claim claim = get(id);
        if (claim.getStatus() != ClaimStatus.OPEN) {
            throw new IllegalStateException("Claim not editable");
        }
        if (description != null) claim.setDescription(description);
        if (lossDate != null) claim.setLossDate(lossDate);
        return claimRepository.save(claim);
    }

    @Transactional
    public Claim assess(String id, String decision, Integer approvedAmount, String reason) {
        Claim claim = get(id);
        if (claim.getStatus() != ClaimStatus.OPEN) {
            throw new IllegalStateException("Claim not open");
        }
        if ("APPROVE".equalsIgnoreCase(decision)) {
            claim.setStatus(ClaimStatus.APPROVED);
            claim.setApprovedAmount(approvedAmount);
            claim.setDecisionReason(reason);
        } else if ("REJECT".equalsIgnoreCase(decision)) {
            claim.setStatus(ClaimStatus.REJECTED);
            claim.setDecisionReason(reason);
            claim.setApprovedAmount(null);
        } else {
            throw new IllegalArgumentException("Invalid decision");
        }
        Claim saved = claimRepository.save(claim);
        notificationPublisher.claimDecision(saved);
        return saved;
    }

    @Transactional
    public Claim close(String id) {
        Claim claim = get(id);
        if (!(claim.getStatus() == ClaimStatus.APPROVED || claim.getStatus() == ClaimStatus.REJECTED)) {
            throw new IllegalStateException("Cannot close");
        }
        claim.setStatus(ClaimStatus.CLOSED);
        return claimRepository.save(claim);
    }

    private String generateClaimNumber() {
        YearMonth ym = YearMonth.now();
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("CLM-%02d%02d-%s", ym.getYear() % 100, ym.getMonthValue(), random);
    }
}
