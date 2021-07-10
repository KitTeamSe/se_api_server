package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.reply.application.dto.ReplyDeleteDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ReplyDeleteService {

    private ReplyJpaRepository replyJpaRepository;
    private PasswordEncoder passwordEncoder;
    private AccountContextService accountContextService;
    // 본인/관리자만 삭제
    @Transactional
    public boolean delete(Long replyId){
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
        Account contextAccount = accountContextService.getContextAccount();
        Set<String> authorities = accountContextService.getContextAuthorities();
        if(reply.getAccount() != contextAccount && !reply.hasManageAuthority(authorities))
            throw new AccessDeniedException("본인 혹은 관리자만 접근 가능");
        reply.delete();
        replyJpaRepository.save(reply);
        return true;
    }

    @Transactional
    public boolean deleteAnonymousReply(ReplyDeleteDto.AnonymousReplyDeleteRequest anonymousReplyDeleteRequest){
        Reply reply = replyJpaRepository.findById(anonymousReplyDeleteRequest.getReplyId())
                .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
        Set<String> authorities = accountContextService.getContextAuthorities();
        if(!reply.hasManageAuthority(authorities)){
            validatePasswordMatch(reply, anonymousReplyDeleteRequest.getPassword());
        }
        reply.delete();
        replyJpaRepository.save(reply);
        return true;
    }

    private void validatePasswordMatch(Reply reply, String password) {
        if(!passwordEncoder.matches(password, reply.getAnonymous().getAnonymousPassword()))
            throw new BusinessException(ReplyErrorCode.INVALID_PASSWORD);
    }

}
