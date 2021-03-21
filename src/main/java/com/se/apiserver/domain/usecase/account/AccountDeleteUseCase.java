package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.http.dto.account.AccountDeleteDto;
import com.se.apiserver.repository.account.AccountJpaRepository;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountDeleteUseCase {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountDetailService accountDetailService;
    @Transactional
    public void delete(AccountDeleteDto.Request request) {
        Account account = accountJpaRepository.findByIdString(request.getId()).orElseThrow(()->new NoSuchAccountException());
        if(!accountDetailService.isOwner(account) && !accountDetailService.hasAuthority("ACCOUNT_MANAGE"))
            throw new AccessDeniedException("비정상적인 접근");
        accountJpaRepository.delete(account);
    }
}
