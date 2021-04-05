package com.se.apiserver.v1.account.infra.repository;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.dto.AccountReadDto;
import org.springframework.data.domain.Page;

public interface AccountQueryRepository {
    Page<Account> search(AccountReadDto.SearchRequest searchRequest);

}
