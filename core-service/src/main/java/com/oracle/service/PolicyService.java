package com.oracle.service;

import com.oracle.entity.Policy;
import com.oracle.entity.PolicyStatus;
import com.oracle.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {
    private final PolicyRepository repository;

    public PolicyService(PolicyRepository repository) {
        this.repository = repository;
    }

    public List<Policy> list(String customerId, PolicyStatus status) {
        if (customerId != null && status != null) {
            return repository.findByCustomer_IdAndStatus(customerId, status);
        } else if (customerId != null) {
            return repository.findByCustomer_Id(customerId);
        } else if (status != null) {
            return repository.findByStatus(status);
        }
        return repository.findAll();
    }

    public Policy get(String id) {
        return repository.findById(id).orElseThrow();
    }
}
