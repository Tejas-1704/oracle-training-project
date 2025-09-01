package com.oracle.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.notification.dto.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final EmailLogRepository repository;
    private final JavaMailSender mailSender;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public EmailService(EmailLogRepository repository, JavaMailSender mailSender, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.mailSender = mailSender;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "notifications.email.send.v1", groupId = "notification-service")
    public void consume(String messageJson) throws Exception {
        EmailMessage message = mapper.readValue(messageJson, EmailMessage.class);
        send(message);
    }

    public void send(EmailMessage message) {
        EmailLog logEntry = new EmailLog();
        logEntry.setId(UUID.randomUUID().toString());
        logEntry.setEventId(message.eventId());
        logEntry.setTemplate(message.template());
        logEntry.setToAddresses(String.join(",", message.to()));
        logEntry.setSubject(message.subject());
        logEntry.setCreatedAt(Instant.now());
        try {
            if (mailSender != null && !message.to().isEmpty()) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(message.to().toArray(new String[0]));
                if (message.cc() != null) {
                    mail.setCc(message.cc().toArray(new String[0]));
                }
                mail.setSubject(message.subject());
                mail.setText(renderBody(message));
                mailSender.send(mail);
            } else {
                log.info("Email to {} subject {}", message.to(), message.subject());
            }
            logEntry.setStatus("SENT");
        } catch (MailException ex) {
            logEntry.setStatus("FAILED");
            logEntry.setError(ex.getMessage());
            try {
                kafkaTemplate.send("notifications.email.dlq.v1", mapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("Failed to send to DLQ", e);
            }
        }
        repository.save(logEntry);
    }

    private String renderBody(EmailMessage msg) {
        String subject = msg.subject();
        if (msg.model() == null) return subject;
        String body = subject;
        for (var entry : msg.model().entrySet()) {
            body = body.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return body;
    }
}
