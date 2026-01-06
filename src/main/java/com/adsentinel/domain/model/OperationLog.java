package com.adsentinel.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String operatorName;

    private String actionType; // e.g., "CREATE", "UPDATE_STATUS"

    private String description;

    private Long entityId;

    private String entityType; // e.g., "GmailAccount", "AdsAccount"
}
