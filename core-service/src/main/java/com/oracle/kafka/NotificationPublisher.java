package com.oracle.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.entity.Claim;
import com.oracle.entity.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class NotificationPublisher {
    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void policyIssued(Policy policy) {
        EmailMessage message = new EmailMessage(
                UUID.randomUUID().toString(),
                "POLICY_ISSUED",
                List.of(policy.getCustomer().getEmail()),
                List.of(),
                "Policy Issued",
                Map.of("policyNumber", policy.getPolicyNumber()),
                Map.of("policyId", policy.getId()),
                null
        );
        send(message);
    }

    public void claimDecision(Claim claim) {
        String template = claim.getStatus().name().equals("APPROVED") ? "CLAIM_APPROVED" : "CLAIM_REJECTED";
        EmailMessage message = new EmailMessage(
                UUID.randomUUID().toString(),
                template,
                List.of(claim.getPolicy().getCustomer().getEmail()),
                List.of(),
                "Claim Decision",
                Map.of(
                        "claimNumber", claim.getClaimNumber(),
                        "decision", claim.getStatus().name()
                ),
                Map.of("claimId", claim.getId()),
                null
        );
        send(message);
    }

    private void send(EmailMessage message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("notifications.email.send.v1", payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize email message", e);
        }
    }
}
