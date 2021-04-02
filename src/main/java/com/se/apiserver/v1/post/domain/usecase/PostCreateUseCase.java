package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCreateUseCase {
    private final PostJpaRepository postJpaRepository;
    private final AccountDetailService accountDetailService;
    private final BoardJpaRepository boardJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagJpaRepository tagJpaRepository;
    private final AttachJpaRepository attachJpaRepository;

    @Transactional
    public Long create(PostCreateDto.Request request) {
        Set<String> authorities = accountDetailService.getContextAuthorities();
        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));

        List<Attach> attachList = getAttaches(request.getAttachmentList());
        List<PostTagMapping> tags = getTags(request.getTagList());


        if(accountDetailService.isSignIn()){
            Account contextAccount = accountDetailService.getContextAccount();
            Post post = new Post(contextAccount, board, request.getPostContent(), request.getIsNotice(),
                    request.getIsSecret(), authorities, attachList, tags);
            postJpaRepository.save(post);
            return post.getPostId();
        }

        if(request.getAnonymous() == null)
            throw new BusinessException(PostErrorCode.INVALID_INPUT);


        request.getAnonymous().setAnonymousPassword(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
        Post post = new Post(request.getAnonymous(), board, request.getPostContent(), request.getIsNotice()
                ,request.getIsSecret(), authorities, attachList, tags);
        postJpaRepository.save(post);

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
