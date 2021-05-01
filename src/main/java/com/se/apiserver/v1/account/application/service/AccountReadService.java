package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountReadDto;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountQueryRepository;
import com.se.apiserver.v1.menu.application.dto.MenuReadDto.ReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.se.apiserver.v1.account.application.dto.AccountReadDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountReadService {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountContextService accountContextService;
    private final AccountQueryRepository accountQueryRepository;

    public Response read(String id) {
        Account account = accountJpaRepository.findByIdString(id).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
        return Response.fromEntity(account, isLegalAccess(account));
    }

    private boolean isLegalAccess(Account account) {
        if(accountContextService.isOwner(account) || accountContextService.hasAuthority(Account.MANAGE_TOKEN))
            return true;
        return false;
    }

    public PageImpl readAll(Pageable pageable) {
        Page<Account> accountPage = accountJpaRepository.findAll(pageable);
        List<Response> res = accountPage.get().map(account -> Response.fromEntity(account, isLegalAccess(account)))
            .collect(Collectors.toList());
        return new PageImpl(res, accountPage.getPageable(), accountPage.getTotalElements());
    }




    public PageImpl search(AccountReadDto.SearchRequest pageRequest) {
        Page<Account> accountPage = accountQueryRepository.search(pageRequest);
        List<Response> res = accountPage.get().map(account -> Response.fromEntity(account, isLegalAccess(account)))
                .collect(Collectors.toList());
        return new PageImpl(res, accountPage.getPageable(), accountPage.getTotalElements());
    }
}

