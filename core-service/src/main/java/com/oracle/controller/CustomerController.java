package com.oracle.controller;

import com.oracle.entity.Customer;
import com.oracle.service.CustomerService;
import com.oracle.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService service;
    private final JwtService jwtService;

    public CustomerController(CustomerService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
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
    public Customer get(HttpServletRequest request) {
        String id = currentUserId(request);
        return service.get(id);
    }

    @PatchMapping("/me")
    public Customer update(HttpServletRequest request, @Valid @RequestBody Customer customer) {
        String id = currentUserId(request);
        return service.update(id, customer);
    }

    @DeleteMapping("/me")
    public java.util.Map<String, Boolean> delete(HttpServletRequest request) {
        String id = currentUserId(request);
        service.delete(id);
        return java.util.Map.of("deleted", true);
    }

    private String currentUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : "";
        return jwtService.extractUserId(token);
    }
}
