package com.adsentinel.application.mapper;

import com.adsentinel.application.dto.GmailAccountDTO;
import com.adsentinel.domain.model.GmailAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GmailMapper {
    @Mapping(target = "email", expression = "java(account.getEmail().getValue())")
    GmailAccountDTO toDTO(GmailAccount account);
}
