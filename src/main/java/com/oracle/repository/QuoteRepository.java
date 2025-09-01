package com.oracle.repository;

import com.oracle.entity.Quote;
import com.oracle.entity.QuoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, String> {
    List<Quote> findByCustomer_Id(String customerId);
    List<Quote> findByStatus(QuoteStatus status);
    List<Quote> findByCustomer_IdAndStatus(String customerId, QuoteStatus status);
}
