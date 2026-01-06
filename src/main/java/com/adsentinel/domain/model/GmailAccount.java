package com.adsentinel.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class GmailAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "email", unique = true, nullable = false))
    })
    private EmailAddress email;

    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String notes;

    private int warmingDays; // Days since creation/warming start

    public enum AccountStatus {
        WARMING,
        ACTIVE,
        SUSPENDED,
        DISABLED
    }
}
