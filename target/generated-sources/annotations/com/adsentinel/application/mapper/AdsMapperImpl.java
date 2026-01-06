package com.adsentinel.application.mapper;

import com.adsentinel.application.dto.AdsAccountDTO;
import com.adsentinel.domain.model.AdsAccount;
import com.adsentinel.domain.model.GmailAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-06T18:58:23-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AdsMapperImpl implements AdsMapper {

    @Override
    public AdsAccountDTO toDTO(AdsAccount account) {
        if ( account == null ) {
            return null;
        }

        AdsAccountDTO adsAccountDTO = new AdsAccountDTO();

        adsAccountDTO.setGmailAccountId( accountGmailAccountId( account ) );
        adsAccountDTO.setChecklistCompleted( account.isChecklistCompleted() );
        adsAccountDTO.setCreationDate( account.getCreationDate() );
        adsAccountDTO.setId( account.getId() );
        adsAccountDTO.setPhase( account.getPhase() );
        adsAccountDTO.setStatus( account.getStatus() );

        adsAccountDTO.setAccountId( account.getAccountId().getValue() );
        adsAccountDTO.setGmailEmail( account.getGmailAccount().getEmail().getValue() );

        return adsAccountDTO;
    }

    private Long accountGmailAccountId(AdsAccount adsAccount) {
        if ( adsAccount == null ) {
            return null;
        }
        GmailAccount gmailAccount = adsAccount.getGmailAccount();
        if ( gmailAccount == null ) {
            return null;
        }
        Long id = gmailAccount.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
