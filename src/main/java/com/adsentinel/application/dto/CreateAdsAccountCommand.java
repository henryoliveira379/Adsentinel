package com.adsentinel.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAdsAccountCommand {
    @NotBlank(message = "Ads Account ID is required")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Invalid Google Ads ID format (expected 123-456-7890)")
    private String accountId;

    @NotNull(message = "Gmail Account ID is required")
    private Long gmailAccountId;

    @NotBlank(message = "Operator name is required")
    private String operatorName;
}
