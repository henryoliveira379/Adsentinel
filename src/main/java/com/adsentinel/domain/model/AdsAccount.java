package com.adsentinel.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class AdsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "account_id", unique = true, nullable = false))
    })
    private GoogleAdsId accountId;

    @ManyToOne
    @JoinColumn(name = "gmail_account_id")
    private GmailAccount gmailAccount;

    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private AdsStatus status;

    private String phase; // e.g., "Phase 1 - Initial", "Phase 2 - Scaling"

    // Checklist status (simplified as boolean flags or a JSON string in real app)
    private boolean checklistCompleted;

    public enum AdsStatus {
        CREATED,
        WARMING,
        ACTIVE,
        PAUSED,
        RISK,
        SUSPENDED
    }
}
