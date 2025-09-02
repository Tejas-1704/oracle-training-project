package com.oracle.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog {
    @Id
    private String id;
    private String eventId;
    private String template;
    private String toAddresses;
    private String subject;
    private String status;
    private String error;
    private Instant createdAt;
}
