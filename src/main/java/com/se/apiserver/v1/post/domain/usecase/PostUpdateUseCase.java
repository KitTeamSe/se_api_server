package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostTagMapping;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.dto.PostUpdateDto;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostUpdateUseCase {
    private final PostJpaRepository postJpaRepository;
    private final AccountDetailService accountDetailService;
    private final BoardJpaRepository boardJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagJpaRepository tagJpaRepository;
    private final AttachJpaRepository attachJpaRepository;

    @Transactional
    public Long update(PostUpdateDto.Request request) {
        Set<String> authorities = accountDetailService.getContextAuthorities();
        Post post = postJpaRepository.findById(request.getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        if(accountDetailService.isSignIn()){
            Account contextAccount = accountDetailService.getContextAccount();
            post.validateAccountAccess(contextAccount, authorities);
        }

        if(request.getAnonymousPassword() != null){
            validateAnonymousAccess(post.getAnonymous(), request.getAnonymousPassword());
        }

        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));

        List<Attach> attachList = getAttaches(request.getAttachmentList());
        List<PostTagMapping> tags = getTags(request.getTagList());

        post.update(board, request.getPostContent(), request.getIsNotice(), request.getIsSecret(), attachList, tags, authorities);

        postJpaRepository.save(post);
        return post.getPostId();
    }

    private void validateAnonymousAccess(Anonymous anonymous, String rowAnonymousPassword) {
        if(!passwordEncoder.matches(rowAnonymousPassword, anonymous.getAnonymousPassword()))
            throw new BusinessException(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT);
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
