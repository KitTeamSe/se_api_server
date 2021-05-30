package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @Transactional
    public Long create(PostCreateDto.Request request) {
        Post post = createPost(request);
        post.validateReadable();
        postJpaRepository.save(post);
        return post.getPostId();
    }

    private Post createPost(PostCreateDto.Request request) {
        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        Set<String> authorities = accountContextService.getContextAuthorities();
        List<Tag> tags = getTagsIfSignIn(request.getTagList());

        List<Attach> attaches = getAttaches(request.getAttachmentList());
        String ip = accountContextService.getCurrentClientIP();
        if(accountContextService.isSignIn()){
            Account contextAccount = accountContextService.getContextAccount();
            Post post = new Post(contextAccount, board, request.getPostContent(), request.getIsNotice(),
                    request.getIsSecret(), authorities, tags, attaches, ip);
            postJpaRepository.save(post);
            return post;
        }

        validateAnonymousInput(request);
        request.getAnonymous().setAnonymousPassword(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
        Post post = new Post(request.getAnonymous(), board, request.getPostContent(), request.getIsNotice()
                ,request.getIsSecret(), authorities, tags, attaches, ip);
        return post;
    }

    private void validateAnonymousInput(PostCreateDto.Request request) {
        if(request.getAnonymous() == null)
            throw new BusinessException(PostErrorCode.INVALID_INPUT);
    }

    // only signed user can add tags
    private List<Tag> getTagsIfSignIn(List<PostCreateDto.TagDto> tagList) {
        if(tagList == null || tagList.size() == 0)
            return new ArrayList<>();
        if(!accountContextService.isSignIn())
            throw new BusinessException(TagErrorCode.ANONYMOUS_CAN_NOT_TAG);
        return tagList.stream()
                .map(t ->
                        tagJpaRepository.findById(t.getTagId())
                                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG))
                )
                .collect(Collectors.toList());
    }

    private List<Attach> getAttaches(List<PostCreateDto.AttachDto> attachmentList) {
        if(attachmentList == null || attachmentList.size() == 0)
            return new ArrayList<>();
        return attachmentList.stream()
                .map(a -> attachJpaRepository.findById(a.getAttachId())
                        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH))
                )
                .collect(Collectors.toList());
    }
}
