package com.se.apiserver.repository.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.http.dto.account.AccountReadDto;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface AccountQueryRepository {
    Page<Account> search(AccountReadDto.SearchRequest searchRequest);

}
