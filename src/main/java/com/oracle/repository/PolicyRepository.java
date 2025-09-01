package com.oracle.repository;

import com.oracle.entity.Policy;
import com.oracle.entity.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, String> {
    List<Policy> findByCustomer_Id(String customerId);
    List<Policy> findByStatus(PolicyStatus status);
    List<Policy> findByCustomer_IdAndStatus(String customerId, PolicyStatus status);
}
