package com.adsentinel.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGmailAccountCommand {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String notes;
    
    @NotBlank(message = "Operator name is required")
    private String operatorName;
}
