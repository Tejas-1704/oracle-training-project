package com.oracle.payment;

import com.oracle.payment.dto.PaymentCreateRequest;
import com.oracle.payment.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResponse create(@RequestBody @Valid PaymentCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<PaymentResponse> list(@RequestParam String targetType, @RequestParam String targetId) {
        return service.list(targetType, targetId);
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable String id) {
        return service.get(id);
    }
}
