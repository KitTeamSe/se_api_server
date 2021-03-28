package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.dto.AccountReadDto;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.AccountQueryRepository;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        Account account = accountJpaRepository.findByIdString(id).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
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
                    .phoneNumber(account.getPhoneNumber())
                    .studentId(account.getStudentId())
                    .informationOpenAgree(account.getInformationOpenAgree());
        }

        if (hasAccountManageAuth) {
            responseBuilder
                    .lastSignInIp(account.getLastSignInIp())
                    .accountId(account.getAccountId());
        }

        return responseBuilder.build();
    }

    public PageImpl readAll(Pageable pageable) {
        Page<Account> accountPage = accountJpaRepository.findAll(pageable);
        List<Response> res = accountPage.get().map(account -> buildResponseDto(account, false, true))
            .collect(Collectors.toList());
        return new PageImpl(res, accountPage.getPageable(), accountPage.getTotalElements());
    }




    public PageImpl search(AccountReadDto.SearchRequest pageRequest) {
        Page<Account> accountPage = accountQueryRepository.search(pageRequest);
        List<Response> res = accountPage.get().map(account -> buildResponseDto(account, false, true))
                .collect(Collectors.toList());
        return new PageImpl(res, accountPage.getPageable(), accountPage.getTotalElements());
    }
}

