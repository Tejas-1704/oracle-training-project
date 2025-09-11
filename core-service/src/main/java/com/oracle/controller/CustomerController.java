package com.oracle.controller;

import com.oracle.entity.Customer;
import com.oracle.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Customer create(@RequestBody Customer customer) {
        return service.create(customer);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Customer> list(@RequestParam(value = "q", required = false) String q) {
        return service.list(q);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Customer get(@PathVariable String id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Customer update(@PathVariable String id, @RequestBody Customer customer) {
        return service.update(id, customer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public java.util.Map<String, Boolean> delete(@PathVariable String id) {
        service.delete(id);
        return java.util.Map.of("deleted", true);
    }
}
