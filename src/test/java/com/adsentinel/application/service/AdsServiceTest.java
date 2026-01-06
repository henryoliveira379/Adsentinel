package com.adsentinel.application.service;

import com.adsentinel.application.dto.AdsAccountDTO;
import com.adsentinel.application.mapper.AdsMapper;
import com.adsentinel.domain.exception.BusinessRuleException;
import com.adsentinel.domain.model.AdsAccount;
import com.adsentinel.domain.model.GmailAccount;
import com.adsentinel.domain.model.GoogleAdsId;
import com.adsentinel.infrastructure.persistence.AdsAccountRepository;
import com.adsentinel.infrastructure.persistence.GmailAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceTest {

    @Mock
    private AdsAccountRepository adsRepository;

    @Mock
    private GmailAccountRepository gmailRepository;

    @Mock
    private OperationLogService logService;

    @Mock
    private AdsMapper adsMapper;

    @InjectMocks
    private AdsService adsService;

    private AdsAccount adsAccount;
    private GmailAccount gmailAccount;

    @BeforeEach
    void setUp() {
        gmailAccount = new GmailAccount();
        gmailAccount.setId(1L);

        adsAccount = new AdsAccount();
        adsAccount.setId(10L);
        adsAccount.setAccountId(new GoogleAdsId("123-456-7890"));
        adsAccount.setGmailAccount(gmailAccount);
        adsAccount.setStatus(AdsAccount.AdsStatus.CREATED);
        adsAccount.setChecklistCompleted(false);
    }

    @Test
    void shouldThrowException_WhenPromotingToActive_WithoutChecklist() {
        // Arrange
        when(adsRepository.findById(10L)).thenReturn(Optional.of(adsAccount));

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            adsService.promoteToActive(10L, "TEST_USER");
        });

        assertEquals("Cannot activate account without completing the mandatory security checklist.", exception.getMessage());
        verify(adsRepository, never()).save(any());
    }

    @Test
    void shouldPromoteToActive_WhenChecklistIsCompleted() {
        // Arrange
        adsAccount.setChecklistCompleted(true);
        when(adsRepository.findById(10L)).thenReturn(Optional.of(adsAccount));
        when(adsRepository.save(any(AdsAccount.class))).thenReturn(adsAccount);
        
        AdsAccountDTO expectedDTO = new AdsAccountDTO();
        expectedDTO.setStatus(AdsAccount.AdsStatus.ACTIVE);
        when(adsMapper.toDTO(any(AdsAccount.class))).thenReturn(expectedDTO);

        // Act
        AdsAccountDTO result = adsService.promoteToActive(10L, "TEST_USER");

        // Assert
        assertNotNull(result);
        assertEquals(AdsAccount.AdsStatus.ACTIVE, result.getStatus());
        verify(adsRepository).save(adsAccount);
        verify(logService).log(eq("TEST_USER"), eq("PROMOTE_ACTIVE"), anyString(), eq(10L), eq("AdsAccount"));
    }
}
