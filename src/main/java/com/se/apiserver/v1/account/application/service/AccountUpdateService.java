package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.application.dto.AccountUpdateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountUpdateService {

    private final AccountJpaRepository accountJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionJpaRepository questionJpaRepository;
    private final AccountContextService accountContextService;

    @Transactional
    public void update(AccountUpdateDto.Request request) {
        Account account = accountJpaRepository.findByIdString(request.getId()).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
        checkInvalidAccess(account, Account.MANAGE_TOKEN);
        updateNameIfNotNull(account, request.getName());
        updateTypeIfNotNull(account, request.getType());
        updateInformationAgreeIfNotNull(account, request.getInformationOpenAgree());
        updatePasswordIfNotEmpty(account, request.getPassword());
        updateNicknameIfNotNull(account,request.getNickname());
        updateQnaIfValid(account, request.getQuestionId(), request.getAnswer());
        updateStudentIdIfNotNull(account, request.getStudentId());
        accountJpaRepository.save(account);
    }

    private void updateStudentIdIfNotNull(Account account, String studentId) {
        if(studentId != null){
            validateDuplicatedStudentId(studentId);
            account.updateStudentId(studentId);
        }
    }

    private void validateDuplicatedStudentId(String studentId) {
        if(accountJpaRepository.findByStudentId(studentId).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_STUDENT_ID);
    }

    private void checkInvalidAccess(Account account, String manageToken) {
        if(!accountContextService.isOwner(account) && !accountContextService.hasAuthority(manageToken))
            throw new AccessDeniedException("비정상적인 접근");
    }

    private void updateQnaIfValid(Account account, Long questionId, String answer) {
        if(questionId == null || answer == null)
            throw new BusinessException(AccountErrorCode.QNA_INVALID_INPUT);
        Question question = questionJpaRepository.findById(questionId).orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_QUESTION));
        account.updateQnA(question, answer);
    }

    private void updateNicknameIfNotNull(Account account, String nickname) {
        if(nickname != null){
            validateDuplicatedNickname(nickname);
            account.updateNickname(nickname);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if(accountJpaRepository.findByNickname(nickname).isPresent())
            throw new BusinessException(AccountErrorCode.DUPLICATED_NICKNAME);
    }

    private void updatePasswordIfNotEmpty(Account account, String password) {
        if(password != null && password.length() > 0){
            account.updatePassword(passwordEncoder.encode(password));
        }
    }

    private void updateInformationAgreeIfNotNull(Account account, InformationOpenAgree informationOpenAgree) {
        if(informationOpenAgree != null){
            account.updateInformationOpenAgree(informationOpenAgree);
        }
    }

    private void updateTypeIfNotNull(Account account, AccountType type) {
        if(type != null){
            account.updateType(type);
        }
    }

    private void updateNameIfNotNull(Account account, String name) {
        if(name != null){
            account.updateName(name);
        }
    }
}
