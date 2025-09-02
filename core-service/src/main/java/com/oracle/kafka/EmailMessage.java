package com.oracle.kafka;

import java.util.List;
import java.util.Map;

public record EmailMessage(
        String eventId,
        String template,
        List<String> to,
        List<String> cc,
        String subject,
        Map<String, Object> model,
        Map<String, String> metadata,
        String sendAfter
) {}
