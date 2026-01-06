package com.adsentinel.application.service;

import com.adsentinel.application.dto.AdsAccountDTO;
import com.adsentinel.application.dto.CreateAdsAccountCommand;
import com.adsentinel.application.mapper.AdsMapper;
import com.adsentinel.domain.exception.BusinessRuleException;
import com.adsentinel.domain.exception.ResourceNotFoundException;
import com.adsentinel.domain.model.AdsAccount;
import com.adsentinel.domain.model.GmailAccount;
import com.adsentinel.domain.model.GoogleAdsId;
import com.adsentinel.infrastructure.persistence.AdsAccountRepository;
import com.adsentinel.infrastructure.persistence.GmailAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdsService {

    private final AdsAccountRepository adsRepository;
    private final GmailAccountRepository gmailRepository;
    private final OperationLogService logService;
    private final AdsMapper adsMapper;

    public AdsService(AdsAccountRepository adsRepository, GmailAccountRepository gmailRepository, OperationLogService logService, AdsMapper adsMapper) {
        this.adsRepository = adsRepository;
        this.gmailRepository = gmailRepository;
        this.logService = logService;
        this.adsMapper = adsMapper;
    }

    @Transactional
    public AdsAccountDTO createAccount(CreateAdsAccountCommand command) {
        GmailAccount gmail = gmailRepository.findById(command.getGmailAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Gmail Account not found with ID: " + command.getGmailAccountId()));

        if (adsRepository.findByAccountIdValue(command.getAccountId()).isPresent()) {
             throw new BusinessRuleException("Ads Account ID already exists: " + command.getAccountId());
        }

        AdsAccount account = new AdsAccount();
        account.setAccountId(new GoogleAdsId(command.getAccountId()));
        account.setGmailAccount(gmail);
        account.setCreationDate(LocalDateTime.now());
        account.setStatus(AdsAccount.AdsStatus.CREATED);
        account.setPhase("Phase 1 - Setup");
        account.setChecklistCompleted(false);

        AdsAccount saved = adsRepository.save(account);
        logService.log(command.getOperatorName(), "CREATE", "Created Ads Account: " + command.getAccountId(), saved.getId(), "AdsAccount");
        return adsMapper.toDTO(saved);
    }

    @Transactional
    public AdsAccountDTO promoteToActive(Long id, String operatorName) {
        AdsAccount account = findAccountById(id);
        
        if (account.getStatus() != AdsAccount.AdsStatus.CREATED) {
             throw new BusinessRuleException("Only CREATED accounts can be promoted to ACTIVE. Current: " + account.getStatus());
        }

        if (!account.isChecklistCompleted()) {
            throw new BusinessRuleException("Cannot activate account without completing the mandatory security checklist.");
        }

        AdsAccount.AdsStatus oldStatus = account.getStatus();
        account.setStatus(AdsAccount.AdsStatus.ACTIVE);
        account.setPhase("Phase 2 - Running");

        AdsAccount saved = adsRepository.save(account);
        logService.log(operatorName, "PROMOTE_ACTIVE", "Changed status from " + oldStatus + " to ACTIVE", saved.getId(), "AdsAccount");
        return adsMapper.toDTO(saved);
    }
    
    @Transactional
    public AdsAccountDTO suspendAccount(Long id, String operatorName) {
        AdsAccount account = findAccountById(id);
        AdsAccount.AdsStatus oldStatus = account.getStatus();
        
        account.setStatus(AdsAccount.AdsStatus.SUSPENDED);
        account.setPhase("Phase X - Suspended");
        
        AdsAccount saved = adsRepository.save(account);
        logService.log(operatorName, "SUSPEND", "Suspended account from " + oldStatus, saved.getId(), "AdsAccount");
        return adsMapper.toDTO(saved);
    }

    @Transactional
    public void completeChecklist(Long id, String operatorName) {
        AdsAccount account = findAccountById(id);
        if (account.isChecklistCompleted()) {
             throw new BusinessRuleException("Checklist is already completed.");
        }
        
        account.setChecklistCompleted(true);
        adsRepository.save(account);
        logService.log(operatorName, "CHECKLIST", "Completed mandatory checklist", id, "AdsAccount");
    }

    public List<AdsAccountDTO> findAll() {
        return adsRepository.findAll().stream()
                .map(adsMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AdsAccountDTO> findByGmailAccount(Long gmailId) {
        return adsRepository.findByGmailAccountId(gmailId).stream()
                .map(adsMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    private AdsAccount findAccountById(Long id) {
        return adsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ads Account not found with ID: " + id));
    }
}
