package com.se.apiserver.domain.usecase.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.error.account.AccountErrorCode;
import com.se.apiserver.domain.exception.account.NoSuchAccountException;
import com.se.apiserver.domain.usecase.UseCase;
import com.se.apiserver.http.dto.account.AccountFindIdByEmailDto;
import com.se.apiserver.http.dto.account.AccountReadDto;
import com.se.apiserver.http.dto.common.PageRequest;
import com.se.apiserver.repository.account.AccountJpaRepository;
import com.se.apiserver.repository.account.AccountQueryRepository;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static com.se.apiserver.http.dto.account.AccountReadDto.*;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountReadUseCase {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountDetailService accountDetailService;
    private final AccountQueryRepository accountQueryRepository;

    public Response read(String id) {
        Account account = accountJpaRepository.findByIdString(id).orElseThrow(() -> new NoSuchAccountException());
        Response res = buildResponseDto(account, accountDetailService.isOwner(account)
                , accountDetailService.hasAuthority("ACCOUNT_MANAGE"));
        return res;
    }

    private Response buildResponseDto(Account account, boolean isOwner, boolean hasAccountManageAuth) {
        Response.ResponseBuilder responseBuilder = Response.builder();
        responseBuilder
                .idString(account.getIdString())
                .name(account.getNickname())
                .nickname(account.getNickname())
                .email(account.getEmail())
                .type(account.getType());

        if (isOwner || hasAccountManageAuth) {
            responseBuilder
                    .lastSignInIp(account.getLastSignInIp())
                    .accountId(account.getAccountId());
        }

        if (hasAccountManageAuth) {
            responseBuilder
                    .phoneNumber(account.getPhoneNumber())
                    .studentId(account.getStudentId())
                    .informationOpenAgree(account.getInformationOpenAgree());
        }

        return responseBuilder.build();
    }

    public PageImpl readAll(PageRequest pageRequest) {
        Page<Account> accountPage = accountJpaRepository.findAll(pageRequest.of());
        List<Response> res = accountPage.get().map(account -> buildResponseDto(account, false, true))
            .collect(Collectors.toList());
        return new PageImpl(res, accountPage.getPageable(), accountPage.getTotalElements());
    }

    public AccountFindIdByEmailDto.Response readByEmail(String email) {
        Account account = accountJpaRepository.findByEmail(email).orElseThrow(() -> new NoSuchAccountException());
        String transformedId = hideLastTwoCharacter(account.getIdString());
        return new AccountFindIdByEmailDto.Response(transformedId);
    }

    private String hideLastTwoCharacter(String idString) {
        return idString.substring(0, idString.length()-2) + "**";
    }

    public PageImpl search(AccountReadDto.SearchRequest pageRequest) {
        Page<Account> accountPage = accountQueryRepository.search(pageRequest);
        List<Response> res = accountPage.get().map(account -> buildResponseDto(account, false, true))
                .collect(Collectors.toList());
        return new PageImpl(res, accountPage.getPageable(), accountPage.getTotalElements());
    }
}

