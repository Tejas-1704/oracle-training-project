package com.oracle.service;

import com.oracle.entity.Product;
import com.oracle.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(Product product) {
        repository.findByCode(product.getCode()).ifPresent(p -> {
            throw new IllegalArgumentException("Code already exists");
        });
        return repository.save(product);
    }

    public List<Product> list(Boolean active) {
        if (active == null) {
            return repository.findAll();
        }
        return repository.findByIsActive(active);
    }

    public Product get(String id) {
        return repository.findById(id).orElseThrow();
    }

    public Product update(String id, Product updates) {
        Product existing = get(id);
        if (updates.getCode() != null && !updates.getCode().equals(existing.getCode())) {
            repository.findByCode(updates.getCode()).ifPresent(p -> {
                throw new IllegalArgumentException("Code already exists");
            });
            existing.setCode(updates.getCode());
        }
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getDescription() != null) existing.setDescription(updates.getDescription());
        if (updates.getBaseRatePer1000() != 0) existing.setBaseRatePer1000(updates.getBaseRatePer1000());
        if (updates.getMinSumAssured() != 0) existing.setMinSumAssured(updates.getMinSumAssured());
        if (updates.getMaxSumAssured() != 0) existing.setMaxSumAssured(updates.getMaxSumAssured());
        if (updates.getMinTermMonths() != 0) existing.setMinTermMonths(updates.getMinTermMonths());
        if (updates.getMaxTermMonths() != 0) existing.setMaxTermMonths(updates.getMaxTermMonths());
//        existing.setIsActive(updates.isActive());
        existing.setActive(updates.isActive());
        return repository.save(existing);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
