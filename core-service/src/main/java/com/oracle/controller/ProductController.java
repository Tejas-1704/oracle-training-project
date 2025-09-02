package com.oracle.controller;

import com.oracle.entity.Product;
import com.oracle.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public Product create(@Valid @RequestBody Product product) {
        return service.create(product);
    }

    @GetMapping
    public List<Product> list(@RequestParam(value = "active", required = false, defaultValue = "false") Boolean active) {
        return service.list(active);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable String id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Product update(@PathVariable String id, @Valid @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    public java.util.Map<String, Boolean> delete(@PathVariable String id) {
        service.delete(id);
        return java.util.Map.of("deleted", true);
    }
}
