package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto;
import com.se.apiserver.v1.reply.application.dto.ReplyUpdateDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReplyUpdateService {
    private ReplyJpaRepository replyJpaRepository;
    private PostJpaRepository postJpaRepository;
    private AccountContextService accountContextService;
    private PasswordEncoder passwordEncoder;
    private AttachJpaRepository attachJpaRepository;

    @Transactional
    public Long update(ReplyUpdateDto.Request request) {
        Reply reply = replyJpaRepository.findById(request.getReplyId())
                .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
        Post post = postJpaRepository.findById(request.getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        post.validateReadable();
        post.getBoard().validateAccessAuthority(accountContextService.getContextAuthorities());

        if(reply.getIsSecret() == ReplyIsSecret.SECRET)
            validatePasswordMatch(reply, request.getPassword());

        updateTextIfNotNull(reply, request.getText());
        updateReplyIsSecretIfNotNull(reply, request.getIsSecret());
        updateAttaches(reply, request.getAttaches());
        updateLastModifiedIp(reply, accountContextService.getCurrentClientIP());
        replyJpaRepository.save(reply);
        return reply.getReplyId();
    }

    private void updateLastModifiedIp(Reply reply, String currentClientIP) {
        reply.updateLastModifiedIp(currentClientIP);
    }

    private void updateAttaches(Reply reply, List<ReplyCreateDto.AttachDto> attaches) {
        if(attaches == null || attaches.isEmpty())
            return;
        List<Attach> attachList = attaches.stream()
                .map(attachDto -> {
                    return attachJpaRepository.findById(attachDto.getAttachId())
                            .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));
                })
                .collect(Collectors.toList());
        reply.updateAttaches(attachList);
    }

    private void updateReplyIsSecretIfNotNull(Reply reply, ReplyIsSecret isSecret) {
        if(isSecret == null)
            return;
        reply.updateIsSecret(isSecret);
    }

    private void updateTextIfNotNull(Reply reply, String text) {
        if(text == null || text.isBlank())
            return;
        reply.updateText(text);
    }

    private void validatePasswordMatch(Reply reply, String password) {
        if(!passwordEncoder.matches(password,reply.getAnonymous().getAnonymousPassword()))
            throw new BusinessException(ReplyErrorCode.INVALID_PASSWORD);
    }
}
