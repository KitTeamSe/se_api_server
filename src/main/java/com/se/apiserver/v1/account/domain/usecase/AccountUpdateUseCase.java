package com.se.apiserver.v1.account.domain.usecase;

import com.se.apiserver.v1.account.domain.exception.InvalidQnAInputException;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.entity.Question;
import com.se.apiserver.v1.account.domain.exception.DuplicatedNicknameException;
import com.se.apiserver.v1.account.domain.exception.DuplicatedStudentIdException;
import com.se.apiserver.v1.account.domain.exception.NoSuchAccountException;
import com.se.apiserver.v1.account.domain.exception.NoSuchQuestion;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.account.infra.dto.AccountUpdateDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.account.infra.repository.QuestionJpaRepository;
import com.se.apiserver.security.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountUpdateUseCase {

    private final AccountJpaRepository accountJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionJpaRepository questionJpaRepository;
    private final AccountDetailService accountDetailService;

    @Transactional
    public void update(AccountUpdateDto.Request request) {
        Account account = accountJpaRepository.findByIdString(request.getId()).orElseThrow(() -> new NoSuchAccountException());

        if(!accountDetailService.isOwner(account) && !accountDetailService.hasAuthority("ACCOUNT_MANAGE"))
            throw new AccessDeniedException("비정상적인 접근");

        account.updateIfNotNull(request.getName(), request.getType(), request.getInformationOpenAgree());

        if(request.getPassword() != null)
            updatePassword(account, request.getPassword());

        if(request.getNickname() != null)
            updateNickname(account, request.getNickname());

        if(request.getQuestionId() == null && request.getAnswer() != null)
            throw new InvalidQnAInputException();

        if(request.getQuestionId() != null && request.getAnswer() == null)
            throw new InvalidQnAInputException();

        if(request.getQuestionId() != null && request.getAnswer() != null)
            updateQnA(account, request.getQuestionId(), request.getAnswer());

        if(request.getStudentId() != null)
            updateStudentId(account, request.getStudentId());
        accountJpaRepository.save(account);
    }

    public void updateStudentId(Account account, String studentId) {
        if(!accountJpaRepository.findByStudentId(studentId).isEmpty())
            throw new DuplicatedStudentIdException();
        account.updateStudentId(studentId);
    }

    public void updateQnA(Account account, Long questionId, String answer) {
        Question question = questionJpaRepository.findById(questionId).orElseThrow(() -> new NoSuchQuestion());
        account.updateQnA(question, answer);
    }

    public void updateNickname(Account account, String nickname) {
        if(!accountJpaRepository.findByNickname(nickname).isEmpty())
            throw new DuplicatedNicknameException();
        account.updateNickname(nickname);
    }

    public void updatePassword(Account account, String rowPassword) {
        account.changePassword(passwordEncoder.encode(rowPassword));
    }
}
