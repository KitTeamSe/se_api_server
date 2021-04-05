package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountFindIdByEmailDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountFindIdService {

    private final AccountJpaRepository accountJpaRepository;
    public AccountFindIdByEmailDto.Response readByEmail(String email) {
        Account account = accountJpaRepository.findByEmail(email).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
        String transformedId = hideLastTwoCharacter(account.getIdString());
        return new AccountFindIdByEmailDto.Response(transformedId);
    }

    public String hideLastTwoCharacter(String idString) {
        return idString.substring(0, idString.length()-2) + "**";
    }
}
