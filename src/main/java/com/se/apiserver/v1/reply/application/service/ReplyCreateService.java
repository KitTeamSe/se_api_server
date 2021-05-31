package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReplyCreateService {
    private ReplyJpaRepository replyJpaRepository;
    private PostJpaRepository postJpaRepository;
    private AccountContextService accountContextService;
    private AttachJpaRepository attachJpaRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Long create(ReplyCreateDto.Request request) {
        Post post = postJpaRepository.findById(request.getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        List<Attach> attachList = request.getAttaches().stream()
                .map(attachDto -> {
                    return attachJpaRepository.findById(attachDto.getAttachId())
                            .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));
                })
                .collect(Collectors.toList());
        Set<String> authorities = accountContextService.getContextAuthorities();
        post.validateReadable();
        post.getBoard().validateAccessAuthority(authorities);
        Reply parent = getParent(request.getParentId());
        String ip = accountContextService.getCurrentClientIP();

        if(accountContextService.isSignIn()){
            Account account = accountContextService.getContextAccount();
            Reply reply = new Reply(post,request.getText(), request.getIsSecret(), attachList, parent, ip, account);
            replyJpaRepository.save(reply);
            return reply.getReplyId();
        }

        request.getAnonymous().setAnonymousPassword(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
        Reply reply = new Reply(post,request.getText(), request.getIsSecret(), attachList, parent, ip, request.getAnonymous());
        replyJpaRepository.save(reply);
        return reply.getReplyId();
    }

    private Reply getParent(Long parentId) {
        if(parentId == null)
            return null;

        Reply parent = replyJpaRepository.findById(parentId)
                .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
        return parent;
    }
}
