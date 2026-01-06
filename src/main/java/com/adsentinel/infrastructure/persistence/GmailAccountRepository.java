package com.adsentinel.infrastructure.persistence;

import com.adsentinel.domain.model.GmailAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface GmailAccountRepository extends JpaRepository<GmailAccount, Long> {
    List<GmailAccount> findByStatus(GmailAccount.AccountStatus status);
    Optional<GmailAccount> findByEmailValue(String email);
}
