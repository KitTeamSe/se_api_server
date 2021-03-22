package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.dto.AccountCreateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class AccountCreateUseCase {

    private final AccountJpaRepository accountJpaRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(AccountCreateDto.Request request, HttpServletRequest httpServletRequest) {
        Account account = Account.builder()
                .idString(request.getId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .studentId(request.getStudentId())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .lastSignInIp(httpServletRequest.getRemoteAddr())
                .informationOpenAgree(InformationOpenAgree.DISAGREE)
                .type(AccountType.ASSISTANT)
                .build();
        accountJpaRepository.save(account);
        return account.getAccountId();
    }

}
