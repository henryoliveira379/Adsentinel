package com.adsentinel.application.service;

import com.adsentinel.application.dto.CreateGmailAccountCommand;
import com.adsentinel.application.dto.GmailAccountDTO;
import com.adsentinel.application.mapper.GmailMapper;
import com.adsentinel.domain.exception.BusinessRuleException;
import com.adsentinel.domain.exception.ResourceNotFoundException;
import com.adsentinel.domain.model.EmailAddress;
import com.adsentinel.domain.model.GmailAccount;
import com.adsentinel.infrastructure.persistence.GmailAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GmailService {

    private final GmailAccountRepository gmailRepository;
    private final OperationLogService logService;
    private final GmailMapper gmailMapper;

    public GmailService(GmailAccountRepository gmailRepository, OperationLogService logService, GmailMapper gmailMapper) {
        this.gmailRepository = gmailRepository;
        this.logService = logService;
        this.gmailMapper = gmailMapper;
    }

    @Transactional
    public GmailAccountDTO createAccount(CreateGmailAccountCommand command) {
        if (gmailRepository.findByEmailValue(command.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email already exists: " + command.getEmail());
        }

        GmailAccount account = new GmailAccount();
        account.setEmail(new EmailAddress(command.getEmail()));
        account.setNotes(command.getNotes());
        account.setCreationDate(LocalDateTime.now());
        account.setStatus(GmailAccount.AccountStatus.WARMING);
        account.setWarmingDays(0);

        GmailAccount saved = gmailRepository.save(account);
        logService.log(command.getOperatorName(), "CREATE", "Created Gmail Account: " + command.getEmail(), saved.getId(), "GmailAccount");
        return gmailMapper.toDTO(saved);
    }

    @Transactional
    public GmailAccountDTO promoteToActive(Long id, String operatorName) {
        GmailAccount account = findAccountById(id);
        
        if (account.getStatus() != GmailAccount.AccountStatus.WARMING) {
            throw new BusinessRuleException("Only WARMING accounts can be promoted to ACTIVE. Current status: " + account.getStatus());
        }

        if (account.getWarmingDays() < 7) {
            throw new BusinessRuleException("Account must be warming for at least 7 days before activation. Current: " + account.getWarmingDays());
        }

        GmailAccount.AccountStatus oldStatus = account.getStatus();
        account.setStatus(GmailAccount.AccountStatus.ACTIVE);
        
        GmailAccount saved = gmailRepository.save(account);
        logService.log(operatorName, "PROMOTE_ACTIVE", "Promoted status from " + oldStatus + " to ACTIVE", saved.getId(), "GmailAccount");
        return gmailMapper.toDTO(saved);
    }
    
    @Transactional
    public GmailAccountDTO suspendAccount(Long id, String operatorName, String reason) {
        GmailAccount account = findAccountById(id);
        GmailAccount.AccountStatus oldStatus = account.getStatus();
        
        account.setStatus(GmailAccount.AccountStatus.SUSPENDED);
        account.setNotes(account.getNotes() + " | Suspended: " + reason);
        
        GmailAccount saved = gmailRepository.save(account);
        logService.log(operatorName, "SUSPEND", "Suspended account from " + oldStatus + ". Reason: " + reason, saved.getId(), "GmailAccount");
        return gmailMapper.toDTO(saved);
    }

    public List<GmailAccountDTO> findAll() {
        return gmailRepository.findAll().stream()
                .map(gmailMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public GmailAccountDTO findById(Long id) {
        return gmailMapper.toDTO(findAccountById(id));
    }

    private GmailAccount findAccountById(Long id) {
        return gmailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gmail Account not found with ID: " + id));
    }
    
    // Kept for backward compatibility if needed by internal calls, but should be avoided
    public GmailAccount findEntityById(Long id) {
        return findAccountById(id);
    }
}
