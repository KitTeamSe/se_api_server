package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyReadDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReplyReadService {
    private ReplyJpaRepository replyJpaRepository;
    private AccountContextService accountContextService;
    private PostJpaRepository postJpaRepository;


    // 관리자는 삭제 된것도 읽을 수 있어야함, 나머지는 대치된 값으로 조회
    // 비밀 댓글은 본인, 관리자, 게시글 작성자만 볼 수 있음.
    public ReplyReadDto.Response read(Long replyId) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
        Set<String> authorities = accountContextService.getContextAuthorities();
        /// Boolean isManager = .../
        return ReplyReadDto.Response.fromEntity(reply, Reply.hasManageAuthority(authorities));
    }

    public List<ReplyReadDto.Response> readAllBelongPost(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        Set<String> authorities = accountContextService.getContextAuthorities();
        post.validateBoardAccessAuthority(authorities);
        post.validateReadable();

        List<Reply> replies = replyJpaRepository.findAllBelongPost(post);
        List<Reply> rootReplies = replies.stream()
                .map(reply -> {
                    if(reply.getParent() == null)
                        return null;
                    return reply;
                })
                .filter(reply -> reply != null)
                .collect(Collectors.toList());
        Boolean hasManageAuthority = Reply.hasManageAuthority(authorities);
        List<ReplyReadDto.Response> responseList = rootReplies.stream()
                .map(rootReply -> {
                    return ReplyReadDto.Response.fromEntity(rootReply, hasManageAuthority);
                })
                .collect(Collectors.toList());
        return responseList;
    }
}