package com.se.apiserver.v1.account.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import com.se.apiserver.v1.account.application.dto.AccountReadDto.AccountSearchRequest;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.QAccount;
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
    public Page<Account> search(AccountSearchRequest accountSearchRequest) {
        QAccount account = QAccount.account;

        JPQLQuery query = from(account);
        if(accountSearchRequest.getName() != null){
            query.where(account.name.contains(accountSearchRequest.getName()));
        }
        if(accountSearchRequest.getNickname() != null){
            query.where(account.nickname.contains(accountSearchRequest.getNickname()));
        }
        if(accountSearchRequest.getEmail() != null){
            query.where(account.email.contains(accountSearchRequest.getEmail()));
        }
        if(accountSearchRequest.getStudentId() != null){
            query.where(account.studentId.contains(accountSearchRequest.getStudentId()));
        }
        if(accountSearchRequest.getPhoneNumber() != null){
            query.where(account.phoneNumber.contains(accountSearchRequest.getPhoneNumber()));
        }
        if(accountSearchRequest.getType() != null){
            query.where(account.type.eq(accountSearchRequest.getType()));
        }
        Pageable pageable = accountSearchRequest.getPageRequest().of();
        List<Account> accounts = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();
        return new PageImpl(accounts, pageable, totalCount);
    }
}
