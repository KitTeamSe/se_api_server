package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.application.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    private final MultipartFileUploadService multipartFileUploadService;

    @Transactional
    public Long create(PostCreateDto.Request request, MultipartFile[] files) {
        Post post = createPost(request);

        String[] fileUrls = multipartFileUploadService.upload(files);
        List<Attach> attachList = IntStream.range(0, files.length)
                .mapToObj(idx -> {
                    return new Attach(fileUrls[idx], files[idx].getName());
                }).collect(Collectors.toList());
        post.updateAttaches(attachList);
        postJpaRepository.save(post);
        return post.getPostId();
    }

    private Post createPost(PostCreateDto.Request request) {
        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        Set<String> authorities = accountContextService.getContextAuthorities();
        List<PostTagMapping> tags = getTags(request.getTagList());

        if(accountContextService.isSignIn()){
            Account contextAccount = accountContextService.getContextAccount();
            Post post = new Post(contextAccount, board, request.getPostContent(), request.getIsNotice(),
                    request.getIsSecret(), authorities, tags);
            postJpaRepository.save(post);
            return post;
        }

        validateAnonymousInput(request);
        request.getAnonymous().setAnonymousPassword(passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
        Post post = new Post(request.getAnonymous(), board, request.getPostContent(), request.getIsNotice()
                ,request.getIsSecret(), authorities, tags);
        return post;
    }

    private void validateAnonymousInput(PostCreateDto.Request request) {
        if(request.getAnonymous() == null)
            throw new BusinessException(PostErrorCode.INVALID_INPUT);
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
