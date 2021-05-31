package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.domain.service.NoticeSendService;
import com.se.apiserver.v1.notice.infra.dto.NoticeSendDto;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCreateService {
    private final PostJpaRepository postJpaRepository;
    private final AccountContextService accountContextService;
    private final BoardJpaRepository boardJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagJpaRepository tagJpaRepository;
    private final AttachJpaRepository attachJpaRepository;
    private final NoticeSendService noticeSendService;

    @Transactional
    public Long create(PostCreateDto.Request request) {
        Set<String> authorities = accountContextService.getContextAuthorities();
        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));

        List<Attach> attachList = getAttaches(request.getAttachmentList());
        List<PostTagMapping> tags = getTags(request.getTagList());
        List<Long> tagIdList = new ArrayList<>();

        if(accountContextService.isSignIn()){
            Account contextAccount = accountContextService.getContextAccount();
            Post post = new Post(contextAccount, board, request.getPostContent(), request.getIsNotice(),
                    request.getIsSecret(), authorities, attachList, tags);
            Post save = postJpaRepository.save(post);

            //Notice 호출
            for (PostCreateDto.TagDto tag: request.getTagList()
            ) {
                tagIdList.add(tag.getTagId());
            }

            noticeSendService.postSend(NoticeSendDto.Request.builder()
                    .tagIdList(tagIdList)
                    .postId(save.getPostId())
                    .build());

            return post.getPostId();
        }

        if(request.getAnonymous() == null)
            throw new BusinessException(PostErrorCode.INVALID_INPUT);


        request.getAnonymous().setAnonymousPassword(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
        Post post = new Post(request.getAnonymous(), board, request.getPostContent(), request.getIsNotice()
                ,request.getIsSecret(), authorities, attachList, tags);
        postJpaRepository.save(post);


        //Notice 호출
        for (PostCreateDto.TagDto tag: request.getTagList()
             ) {
            tagIdList.add(tag.getTagId());
        }

        noticeSendService.postSend(NoticeSendDto.Request.builder()
                            .tagIdList(tagIdList)
                            .postId(post.getPostId())
                            .build());

        return post.getPostId();
    }

    private List<PostTagMapping> getTags(List<PostCreateDto.TagDto> tagList) {
        return tagList.stream()
                .map(t -> PostTagMapping.builder()
                        .tag(tagJpaRepository.findById(t.getTagId())
                                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG)))
                        .build())
                .collect(Collectors.toList());
    }

    private List<Attach> getAttaches(List<PostCreateDto.AttachDto> attachmentList) {
        return attachmentList.stream()
                .map(a -> attachJpaRepository.findById(a.getAttachId())
                        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH))
                )
                .collect(Collectors.toList());
    }
}
