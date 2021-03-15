package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.repository.account.AccountJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class AccountReadUseCase {

    private final AccountJpaRepository accountJpaRepository;

    public Account read(Long accountId){
        Optional<Account> member = accountJpaRepository.findById(accountId);
        return member.get();
    }

}
