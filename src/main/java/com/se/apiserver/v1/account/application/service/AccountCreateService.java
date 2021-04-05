package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.account.application.dto.AccountCreateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountCreateService {

    private final AccountJpaRepository accountJpaRepository;

    private final QuestionJpaRepository questionJpaRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(AccountCreateDto.Request request, String ip) {
        Question question = questionJpaRepository.findById(request.getQuestionId()).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

        if (accountJpaRepository.findByNickname(request.getNickname()).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_NICKNAME);

        if (accountJpaRepository.findByIdString(request.getId()).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_ID);

        if (accountJpaRepository.findByStudentId(request.getStudentId()).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_STUDENT_ID);

        if (accountJpaRepository.findByEmail(request.getEmail()).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_EMAIL);


        Account account = Account.builder()
                .idString(request.getId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .studentId(request.getStudentId())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .lastSignInIp(ip)
                .informationOpenAgree(InformationOpenAgree.DISAGREE)
                .type(AccountType.ASSISTANT)
                .question(question)
                .answer(request.getAnswer())
                .build();
        accountJpaRepository.save(account);
        return account.getAccountId();
    }

}
