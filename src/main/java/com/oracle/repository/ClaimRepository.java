package com.oracle.repository;

import com.oracle.entity.Claim;
import com.oracle.entity.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, String> {
    List<Claim> findByPolicy_Id(String policyId);
    List<Claim> findByStatus(ClaimStatus status);
    List<Claim> findByPolicy_IdAndStatus(String policyId, ClaimStatus status);
}
