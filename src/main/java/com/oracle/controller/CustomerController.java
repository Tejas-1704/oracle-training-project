package com.oracle.controller;

import com.oracle.entity.Customer;
import com.oracle.service.CustomerService;
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
    public Customer create(@RequestBody Customer customer) {
        return service.create(customer);
    }

    @GetMapping
    public List<Customer> list(@RequestParam(value = "q", required = false) String q) {
        return service.list(q);
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable String id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Customer update(@PathVariable String id, @RequestBody Customer customer) {
        return service.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public java.util.Map<String, Boolean> delete(@PathVariable String id) {
        service.delete(id);
        return java.util.Map.of("deleted", true);
    }
}
