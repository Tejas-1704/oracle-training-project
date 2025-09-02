package com.oracle.notification;

import com.oracle.notification.dto.EmailMessage;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping("/test")
    public Map<String, String> test(@RequestBody @Valid EmailMessage message) {
        service.send(message);
        return Map.of("status", "sent");
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
