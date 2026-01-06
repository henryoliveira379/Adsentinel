package com.adsentinel.application.mapper;

import com.adsentinel.application.dto.GmailAccountDTO;
import com.adsentinel.domain.model.GmailAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-06T18:58:22-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class GmailMapperImpl implements GmailMapper {

    @Override
    public GmailAccountDTO toDTO(GmailAccount account) {
        if ( account == null ) {
            return null;
        }

        GmailAccountDTO gmailAccountDTO = new GmailAccountDTO();

        gmailAccountDTO.setCreationDate( account.getCreationDate() );
        gmailAccountDTO.setId( account.getId() );
        gmailAccountDTO.setNotes( account.getNotes() );
        gmailAccountDTO.setStatus( account.getStatus() );
        gmailAccountDTO.setWarmingDays( account.getWarmingDays() );

        gmailAccountDTO.setEmail( account.getEmail().getValue() );

        return gmailAccountDTO;
    }
}
