package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.dto.AccountFindIdByEmailDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountFindIdUseCase {

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
