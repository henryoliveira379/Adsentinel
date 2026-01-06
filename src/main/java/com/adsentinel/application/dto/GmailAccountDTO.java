package com.adsentinel.application.dto;

import com.adsentinel.domain.model.GmailAccount;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GmailAccountDTO {
    private Long id;
    private String email;
    private LocalDateTime creationDate;
    private GmailAccount.AccountStatus status;
    private String notes;
    private int warmingDays;
}
