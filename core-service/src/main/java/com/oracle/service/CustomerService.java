package com.oracle.service;

import com.oracle.entity.Customer;
import com.oracle.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(Customer customer) {
        repository.findByEmail(customer.getEmail()).ifPresent(c -> {
            throw new IllegalArgumentException("Email already exists");
        });
        return repository.save(customer);
    }

    public List<Customer> list(String query) {
        List<Customer> all = repository.findAll();
        if (query == null || query.isBlank()) {
            return all;
        }
        String q = query.toLowerCase();
        return all.stream().filter(c ->
                (c.getFirstName() != null && c.getFirstName().toLowerCase().contains(q)) ||
                (c.getLastName() != null && c.getLastName().toLowerCase().contains(q)) ||
                (c.getEmail() != null && c.getEmail().toLowerCase().contains(q))
        ).collect(Collectors.toList());
    }

    public Customer get(String id) {
        return repository.findById(id).orElseThrow();
    }

    public Customer update(String id, Customer updates) {
        Customer existing = get(id);
        if (updates.getEmail() != null && !updates.getEmail().equals(existing.getEmail())) {
            repository.findByEmail(updates.getEmail()).ifPresent(c -> {
                throw new IllegalArgumentException("Email already exists");
            });
            existing.setEmail(updates.getEmail());
        }
        if (updates.getFirstName() != null) existing.setFirstName(updates.getFirstName());
        if (updates.getLastName() != null) existing.setLastName(updates.getLastName());
        if (updates.getPhone() != null) existing.setPhone(updates.getPhone());
        if (updates.getDob() != null) existing.setDob(updates.getDob());
        return repository.save(existing);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
