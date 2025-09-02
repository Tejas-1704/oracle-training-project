package com.oracle.payment;

import com.oracle.payment.dto.PaymentCreateRequest;
import com.oracle.payment.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public PaymentResponse create(PaymentCreateRequest request) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setTargetType(request.getTargetType());
        payment.setTargetId(request.getTargetId());
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus("SUCCEEDED");
        payment.setGatewayRef("DUMMY-" + payment.getId().substring(0,8));
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());
        repository.save(payment);
        return toDto(payment);
    }

    public List<PaymentResponse> list(String targetType, String targetId) {
        return repository.findByTargetTypeAndTargetId(targetType, targetId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public PaymentResponse get(String id) {
        return repository.findById(id).map(this::toDto).orElseThrow();
    }

    private PaymentResponse toDto(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .targetType(payment.getTargetType())
                .targetId(payment.getTargetId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .gatewayRef(payment.getGatewayRef())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
