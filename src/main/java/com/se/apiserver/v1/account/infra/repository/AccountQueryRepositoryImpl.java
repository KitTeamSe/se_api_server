package com.se.apiserver.v1.account.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.QAccount;
import com.se.apiserver.v1.account.infra.dto.AccountReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountQueryRepositoryImpl extends QuerydslRepositorySupport implements AccountQueryRepository{
    public AccountQueryRepositoryImpl() {
        super(Account.class);
    }

    @Override
    public Page<Account> search(AccountReadDto.SearchRequest searchRequest) {
        QAccount account = QAccount.account;

        JPQLQuery query = from(account);
        if(searchRequest.getName() != null){
            query.where(account.name.contains(searchRequest.getName()));
        }
        if(searchRequest.getNickname() != null){
            query.where(account.nickname.contains(searchRequest.getNickname()));
        }
        if(searchRequest.getEmail() != null){
            query.where(account.email.contains(searchRequest.getEmail()));
        }
        if(searchRequest.getStudentId() != null){
            query.where(account.studentId.contains(searchRequest.getStudentId()));
        }
        if(searchRequest.getPhoneNumber() != null){
            query.where(account.phoneNumber.contains(searchRequest.getPhoneNumber()));
        }
        if(searchRequest.getType() != null){
            query.where(account.type.eq(searchRequest.getType()));
        }
        Pageable pageable = searchRequest.getPageRequest().of();
        List<Account> accounts = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();
        return new PageImpl(accounts, pageable, totalCount);
    }
}
