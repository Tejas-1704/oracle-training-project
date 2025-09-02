package com.oracle.controller;

import com.oracle.entity.Customer;
import com.oracle.service.CustomerService;
import jakarta.validation.Valid;
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
    public Customer create(@Valid @RequestBody Customer customer) {
        return service.create(customer);
    }

    @GetMapping
    public List<Customer> list(@RequestParam(value = "q", required = false) String q) {
        return service.list(q);
    }

    @GetMapping("/me")
    public Customer get(@RequestHeader("X-USER-ID") String id) {
        return service.get(id);
    }

    @PatchMapping("/me")
    public Customer update(@RequestHeader("X-USER-ID") String id, @Valid @RequestBody Customer customer) {
        return service.update(id, customer);
    }

    @DeleteMapping("/me")
    public java.util.Map<String, Boolean> delete(@RequestHeader("X-USER-ID") String id) {
        service.delete(id);
        return java.util.Map.of("deleted", true);
    }
}
