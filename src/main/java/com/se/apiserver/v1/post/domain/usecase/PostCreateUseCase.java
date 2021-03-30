package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.domain.error.AccountErrorCode;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.attach.domain.entity.Attach;
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
    private final AttachJpaRepository attachJpaRepository;

    public PostReadDto.Response create(PostCreateDto.Request request) {
        Board board = boardJpaRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));


        // 1. 게시판에 대한 접근 권한 검사
        if(!hasBoardAccessAuthority(board.getMenu().getAuthority().getAuthority()) && !hasBoardAccessAuthority("MENU_MANAGE"))
            throw new BusinessException(PostErrorCode.CAN_NOT_ACCESS_BOARD);

        // 익명 작성이 아닐때, 본인 계정이 맞는지
        if(request.getAccountId() != null && !accountDetailService.isOwner(request.getAccountId()))
            throw new AccessDeniedException("권한 없음");

        // 비관리자가 공지 설정 요청할 때
        if(request.getIsNotice() == PostIsNotice.NOTICE && !accountDetailService.hasAuthority("MENU_MANAGE"))
            throw new BusinessException(PostErrorCode.ONLY_ADMIN_SET_NOTICE);

        // 익명사용자의 공지 설정 요청
        if(request.getIsNotice() == PostIsNotice.NOTICE && request.getAnonymousNickname() != null)
            throw new BusinessException(PostErrorCode.ONLY_ADMIN_SET_NOTICE);

        // 익명 및 사용자 정보 관련 입력 검증
        if(!validateWriterInfo(request))
            throw new BusinessException(PostErrorCode.INVALID_INPUT);

        Post.PostBuilder postBuilder = Post.builder()
                .title(request.getTitle())
                .text(request.getText())
                .isDeleted(PostIsDeleted.NORMAL)
                .isSecret(request.getIsSecret())
                .board(board)
                .views(0)
                .numReply(0)
                .isNotice(request.getIsNotice());

        // 사용자 정보 설정
        if(request.getAccountId() != null){
            if (request.getAccountId() != null) {
                Account account = accountJpaRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
                postBuilder
                        .account(account);
            }
        }

        // 익명 정보 설정
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

        // 첨부 파일 저장
        if(request.getAttachmentList() != null){

            List<Attach> attachList = request.getAttachmentList().stream()
                    .map(a -> attachJpaRepository.findById(a.getAttachId())
                        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH))
                        )
                    .collect(Collectors.toList());
            post.addAttaches(attachList);
        }

        // 태그 매핑 정보 저장
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

    private boolean hasBoardAccessAuthority(String authority) {
        return accountDetailService.hasAuthority(authority);
    }


    private boolean validateWriterInfo(PostCreateDto.Request request) {
        if (request.getAccountId() == null && request.getAnonymousNickname() == null)
            return false;
        if (request.getAccountId() != null && request.getAnonymousNickname() != null)
            return false;
        return true;
    }

}
