package com.oracle.service;

import com.oracle.entity.*;
import com.oracle.repository.*;
import com.oracle.kafka.NotificationPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
//import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PolicyRepository policyRepository;
    private final NotificationPublisher notificationPublisher;

    public QuoteService(QuoteRepository quoteRepository, CustomerRepository customerRepository,
                        ProductRepository productRepository, PolicyRepository policyRepository,
                        NotificationPublisher notificationPublisher) {
        this.quoteRepository = quoteRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.policyRepository = policyRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public Quote create(String customerId, String productId, int sumAssured, int termMonths) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        validateProduct(product, sumAssured, termMonths);
        Quote quote = new Quote();
        quote.setCustomer(customer);
        quote.setProduct(product);
        quote.setSumAssured(sumAssured);
        quote.setTermMonths(termMonths);
        quote.setStatus(QuoteStatus.DRAFT);
        return quoteRepository.save(quote);
    }

    public List<Quote> list(String customerId, QuoteStatus status) {
        if (customerId != null && status != null) {
            return quoteRepository.findByCustomer_IdAndStatus(customerId, status);
        } else if (customerId != null) {
            return quoteRepository.findByCustomer_Id(customerId);
        } else if (status != null) {
            return quoteRepository.findByStatus(status);
        }
        return quoteRepository.findAll();
    }

    public Quote get(String id) {
        return quoteRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Quote update(String id, Integer sumAssured, Integer termMonths) {
        Quote quote = get(id);
        if (!(quote.getStatus() == QuoteStatus.DRAFT || quote.getStatus() == QuoteStatus.PRICED)) {
            throw new IllegalStateException("Quote not editable");
        }
        if (sumAssured != null) {
            validateProduct(quote.getProduct(), sumAssured, quote.getTermMonths());
            quote.setSumAssured(sumAssured);
        }
        if (termMonths != null) {
            validateProduct(quote.getProduct(), quote.getSumAssured(), termMonths);
            quote.setTermMonths(termMonths);
        }
        quote.setStatus(QuoteStatus.DRAFT);
        quote.setPremiumCached(null);
        quote.setPricingSource(null);
        return quoteRepository.save(quote);
    }

    @Transactional
    public Quote price(String id) {
        Quote quote = get(id);
        if (!(quote.getStatus() == QuoteStatus.DRAFT || quote.getStatus() == QuoteStatus.PRICED)) {
            throw new IllegalStateException("Cannot price quote");
        }
        int premium = (int) Math.round(((double) quote.getSumAssured() / 1000d) * quote.getProduct().getBaseRatePer1000() * (quote.getTermMonths() / 12.0));
        quote.setPremiumCached(premium);
        quote.setPricingSource("LOCAL");
        quote.setStatus(QuoteStatus.PRICED);
        return quoteRepository.save(quote);
    }

    @Transactional
    public Policy confirm(String id) {
        Quote quote = get(id);
        if (quote.getStatus() != QuoteStatus.PRICED || quote.getPremiumCached() == null) {
            throw new IllegalStateException("Quote not priced");
        }
        Policy policy = new Policy();
        policy.setPolicyNumber(generatePolicyNumber());
        policy.setCustomer(quote.getCustomer());
        policy.setProduct(quote.getProduct());
        policy.setSumAssured(quote.getSumAssured());
        policy.setTermMonths(quote.getTermMonths());
        policy.setPremium(quote.getPremiumCached());
        LocalDate start = LocalDate.now();
        policy.setStartDate(start);
        policy.setEndDate(start.plusMonths(quote.getTermMonths()));
        policy.setStatus(PolicyStatus.ACTIVE);
        Policy saved = policyRepository.save(policy);
        quote.setStatus(QuoteStatus.CONFIRMED);
        quoteRepository.save(quote);
        notificationPublisher.policyIssued(saved);
        return saved;
    }

    private void validateProduct(Product product, int sumAssured, int termMonths) {
        if (!product.isActive()) {
            throw new IllegalArgumentException("Product inactive");
        }
        if (sumAssured < product.getMinSumAssured() || sumAssured > product.getMaxSumAssured()) {
            throw new IllegalArgumentException("Sum assured out of bounds");
        }
        if (termMonths < product.getMinTermMonths() || termMonths > product.getMaxTermMonths()) {
            throw new IllegalArgumentException("Term out of bounds");
        }
    }

    private String generatePolicyNumber() {
        YearMonth ym = YearMonth.now();
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("PROT-%02d%02d-%s", ym.getYear() % 100, ym.getMonthValue(), random);
    }
}
