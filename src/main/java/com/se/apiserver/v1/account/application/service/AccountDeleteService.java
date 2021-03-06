package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountDeleteService {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountContextService accountContextService;
    @Transactional
    public void delete(String id) {
        Account account = accountJpaRepository.findByIdString(id).orElseThrow(()->new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
        validateAccessible(account);
        accountJpaRepository.delete(account);
    }

    private void validateAccessible(Account account) {
        if(!accountContextService.isOwner(account) && !accountContextService.hasAuthority("ACCOUNT_MANAGE"))
            throw new AccessDeniedException("비정상적인 접근");
    }
}
