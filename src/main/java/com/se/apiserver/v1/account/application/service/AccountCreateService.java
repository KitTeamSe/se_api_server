package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.account.application.dto.AccountCreateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupReadService;
import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateService;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
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
    private final AuthorityGroupAccountMappingCreateService authorityGroupAccountMappingCreateService;
    private final AuthorityGroupReadService authorityGroupReadService;

    @Transactional
    public Long signUp(AccountCreateDto.Request request, String ip) {
        validateDuplicatedNickname(request.getNickname());
        validateDuplicatedIdString(request.getId());
        validateDuplicatedStudentId(request.getStudentId());
        validateDuplicatedEmail(request.getEmail());

        Question question = questionJpaRepository.findById(request.getQuestionId()).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
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
                .type(request.getType())
                .question(question)
                .answer(request.getAnswer())
                .build();

        accountJpaRepository.save(account);

        mapDefaultAuthorityGroup(account);

        return account.getAccountId();
    }

    private void validateDuplicatedEmail(String email) {
        if (accountJpaRepository.findByEmail(email).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_EMAIL);
    }

    private void validateDuplicatedStudentId(String studentId) {
        if (accountJpaRepository.findByStudentId(studentId).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_STUDENT_ID);
    }

    private void validateDuplicatedIdString(String id) {
        if (accountJpaRepository.findByIdString(id).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_ID);
    }

    private void validateDuplicatedNickname(String nickname) {
        if (accountJpaRepository.findByNickname(nickname).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_NICKNAME);
    }

    private void mapDefaultAuthorityGroup(Account account){
        AuthorityGroup defaultGroup = authorityGroupReadService.getDefaultAuthorityGroup();

        AuthorityGroupAccountMappingCreateDto.Request request = AuthorityGroupAccountMappingCreateDto.Request.builder()
            .accountId(account.getAccountId())
            .groupId(defaultGroup.getAuthorityGroupId())
            .build();
        authorityGroupAccountMappingCreateService.create(request);
    }

}
