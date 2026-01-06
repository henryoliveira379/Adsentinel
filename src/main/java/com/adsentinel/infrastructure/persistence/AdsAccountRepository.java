package com.adsentinel.infrastructure.persistence;

import com.adsentinel.domain.model.AdsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface AdsAccountRepository extends JpaRepository<AdsAccount, Long> {
    List<AdsAccount> findByStatus(AdsAccount.AdsStatus status);
    List<AdsAccount> findByGmailAccountId(Long gmailAccountId);
    Optional<AdsAccount> findByAccountIdValue(String accountId);
}
