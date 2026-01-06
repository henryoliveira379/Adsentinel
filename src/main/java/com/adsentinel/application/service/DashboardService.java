package com.adsentinel.application.service;

import com.adsentinel.domain.model.AdsAccount;
import com.adsentinel.domain.model.GmailAccount;
import com.adsentinel.infrastructure.persistence.AdsAccountRepository;
import com.adsentinel.infrastructure.persistence.GmailAccountRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final GmailAccountRepository gmailRepository;
    private final AdsAccountRepository adsRepository;

    public DashboardService(GmailAccountRepository gmailRepository, AdsAccountRepository adsRepository) {
        this.gmailRepository = gmailRepository;
        this.adsRepository = adsRepository;
    }

    public Map<String, Long> getGmailStats() {
        Map<String, Long> stats = new HashMap<>();
        for (GmailAccount.AccountStatus status : GmailAccount.AccountStatus.values()) {
            stats.put(status.name(), (long) gmailRepository.findByStatus(status).size());
        }
        stats.put("TOTAL", gmailRepository.count());
        return stats;
    }

    public Map<String, Long> getAdsStats() {
        Map<String, Long> stats = new HashMap<>();
        for (AdsAccount.AdsStatus status : AdsAccount.AdsStatus.values()) {
            stats.put(status.name(), (long) adsRepository.findByStatus(status).size());
        }
        stats.put("TOTAL", adsRepository.count());
        return stats;
    }
}
