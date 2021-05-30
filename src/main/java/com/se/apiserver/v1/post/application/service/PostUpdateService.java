package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.post.application.dto.PostUpdateDto;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.domain.entity.PostIsDeleted;
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
public class PostUpdateService {
    private final PostJpaRepository postJpaRepository;
    private final AccountContextService accountContextService;
    private final BoardJpaRepository boardJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagJpaRepository tagJpaRepository;
    private final AttachJpaRepository attachJpaRepository;

    @Transactional
    public Long update(PostUpdateDto.Request request) {
        Set<String> authorities = accountContextService.getContextAuthorities();
        Post post = postJpaRepository.findById(request.getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        Board board = post.getBoard();
        List<Attach> attachList = getAttaches(request.getAttachmentList());
        List<Tag> tags = getTagsIfSignIn(request.getTagList());
        String ip = accountContextService.getCurrentClientIP();

        post.validateReadable();

        if(accountContextService.isSignIn() && post.getAnonymous() == null){
            Account contextAccount = accountContextService.getContextAccount();
            post.validateAccountAccess(contextAccount, authorities);
            post.update(board, request.getPostContent(), request.getIsNotice(), request.getIsSecret(), attachList, tags, authorities, ip);
            postJpaRepository.save(post);
            return post.getPostId();
        }

        if(post.getAnonymous() == null || request.getAnonymousPassword() == null)
            throw new BusinessException(PostErrorCode.INVALID_INPUT);

        validateAnonymousAccess(post.getAnonymous(), request.getAnonymousPassword());

        post.update(board, request.getPostContent(), request.getIsNotice(), request.getIsSecret(), attachList, tags, authorities, ip);
        postJpaRepository.save(post);
        return post.getPostId();
    }

    private void validateAnonymousAccess(Anonymous anonymous, String rowAnonymousPassword) {
        if(!passwordEncoder.matches(rowAnonymousPassword, anonymous.getAnonymousPassword()))
            throw new BusinessException(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT);
    }

    // only signed user can add tags
    private List<Tag> getTagsIfSignIn(List<PostCreateDto.TagDto> tagList) {
        if(tagList == null || tagList.size() == 0)
            return new ArrayList<>();
        if(!accountContextService.isSignIn())
            throw new BusinessException(TagErrorCode.ANONYMOUS_CAN_NOT_TAG);
        return tagList.stream()
                .map(t ->tagJpaRepository.findById(t.getTagId())
                                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG)))
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
