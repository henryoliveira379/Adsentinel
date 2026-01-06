package com.adsentinel.application.dto;

import com.adsentinel.domain.model.AdsAccount;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdsAccountDTO {
    private Long id;
    private String accountId;
    private Long gmailAccountId;
    private String gmailEmail;
    private LocalDateTime creationDate;
    private AdsAccount.AdsStatus status;
    private String phase;
    private boolean checklistCompleted;
}
