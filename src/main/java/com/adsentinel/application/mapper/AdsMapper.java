package com.adsentinel.application.mapper;

import com.adsentinel.application.dto.AdsAccountDTO;
import com.adsentinel.domain.model.AdsAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdsMapper {
    @Mapping(target = "accountId", expression = "java(account.getAccountId().getValue())")
    @Mapping(target = "gmailAccountId", source = "gmailAccount.id")
    @Mapping(target = "gmailEmail", expression = "java(account.getGmailAccount().getEmail().getValue())")
    AdsAccountDTO toDTO(AdsAccount account);
}
