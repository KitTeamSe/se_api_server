package com.se.apiserver.v1.post.domain.usecase;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.domain.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@UseCase
@Transactional(readOnly = true)
public class PostReadUseCase {
    private final PostJpaRepository postJpaRepository;
    private final AccountDetailService accountDetailService;
    private final PasswordEncoder passwordEncoder;
    private final BoardJpaRepository boardJpaRepository;
    public PostReadDto.Response read(Long postId){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        return PostReadDto.Response.fromEntity(post, isOwnerOrHasManageAuthority(post));
    }

    public PostReadDto.Response readAnonymousSecretPost(Long postId, String password){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        if(!passwordEncoder.matches(password, post.getAnonymousPassword()))
            throw new BusinessException(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT);
        return PostReadDto.Response.fromEntity(post, true);
    }

    public PageImpl<PostReadDto.ListResponse> readBoardPostList(Pageable pageable, Long boardId){
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        Set<String> authorities = accountDetailService.getContextAuthorities();
        board.validateAccessAuthority(authorities);

        Page<Post> allByBoard = postJpaRepository.findAllByBoard(board, pageable);
        List<PostReadDto.ListResponse> list = allByBoard.stream()
                .map(p -> PostReadDto.ListResponse.fromEntity(p))
                .collect(Collectors.toList());
        return new PageImpl<>(list, allByBoard.getPageable(), allByBoard.getTotalElements());
    }

    private boolean isOwnerOrHasManageAuthority(Post post) {
        Set<String> authorities = accountDetailService.getContextAuthorities();
        if(authorities.contains(Post.MANAGE_AUTHORITY) || post.isOwner(accountDetailService.getContextAccount()))
            return true;
        return false;
    }
}
