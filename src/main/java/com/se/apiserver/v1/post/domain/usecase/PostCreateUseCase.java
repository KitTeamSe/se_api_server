package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.tag.domain.error.TagErrorCode;
import com.se.apiserver.v1.tag.infra.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCreateUseCase {
    private final PostJpaRepository postJpaRepository;
    private final AccountDetailService accountDetailService;
    private final BoardJpaRepository boardJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagJpaRepository tagJpaRepository;

    public PostReadDto.Response create(PostCreateDto.Request request) {
        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));

        Post.PostBuilder postBuilder = Post.builder()
                .title(request.getTitle())
                .text(request.getText())
                .isDeleted(PostIsDeleted.NORMAL)
                .isSecret(request.getIsSecret())
                .board(board)
                .views(0)
                .numReply(0)
                .isNotice(PostIsNotice.NORMAL);



        boolean isOwner = accountDetailService.isOwner(request.getAccountId());
        boolean hasManageAuthority = accountDetailService.hasAuthority("POST_MANAGE");

        if(hasManageAuthority){
            postBuilder.isNotice(request.getIsNotice());
        }

        if(request.getIsNotice() == PostIsNotice.NOTICE && !hasManageAuthority)
            throw new BusinessException(PostErrorCode.ONLY_ADMIN_SET_NOTICE);

        if(request.getIsNotice() == PostIsNotice.NOTICE && request.getAnonymousNickname() != null)
            throw new BusinessException(PostErrorCode.ONLY_ADMIN_SET_NOTICE);

        if(!validateWriterInfo(request))
            throw new BusinessException(PostErrorCode.INVALID_INPUT);

        if(request.getAccountId() != null){
            if (request.getAccountId() != null && !isOwner)
                throw new AccessDeniedException("비정상적인 접근");

            if (request.getAccountId() != null) {
                Account account = accountJpaRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
                postBuilder
                        .account(account);
            }
        }


        if(request.getAnonymousNickname() != null){
            postBuilder
                    .anonymous(
                            Anonymous.builder()
                                    .anonymousNickname(request.getAnonymousNickname())
                                    .anonymousPassword(passwordEncoder.encode(request.getAnonymousPassword()))
                                    .build()
                    );
        }

        Post post = postBuilder.build();
        if(request.getAttachmentList() != null){

            List<Attach> attachList = request.getAttachmentList().stream()
                    .map(a -> a.toEntity())
                    .collect(Collectors.toList());
            post.addAttaches(attachList);
        }

        if(request.getTagList() != null){
            List<PostTagMapping> postTagMappings = request.getTagList().stream()
                    .map(t -> PostTagMapping.builder()
                            .tag(tagJpaRepository.findById(t.getTagId())
                                    .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG)))
                            .build())
                    .collect(Collectors.toList());
            post.addTags(postTagMappings);
        }



        postJpaRepository.save(post);

        return PostReadDto.Response.fromEntity(post, true);
    }

    private boolean validateWriterInfo(PostCreateDto.Request request) {
        if (request.getAccountId() == null && request.getAnonymousNickname() == null)
            return false;
        if (request.getAccountId() != null && request.getAnonymousNickname() != null)
            return false;
        return true;
    }

}
