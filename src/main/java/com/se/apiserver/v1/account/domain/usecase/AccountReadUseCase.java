package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.exception.NoSuchAccountException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.dto.AccountFindIdByEmailDto;
import com.se.apiserver.v1.account.infra.dto.AccountReadDto;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountQueryRepository;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;

import static com.se.apiserver.v1.account.infra.dto.AccountReadDto.*;

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

