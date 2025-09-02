package com.oracle.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByTargetTypeAndTargetId(String targetType, String targetId);
}
