package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.application.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReadService {
    private final PostJpaRepository postJpaRepository;
    private final AccountContextService accountContextService;
    private final PasswordEncoder passwordEncoder;
    private final BoardJpaRepository boardJpaRepository;
    private final PostQueryRepository postQueryRepository;
    public PostReadDto.Response read(Long postId){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        Board board = post.getBoard();

        Set<String> authorities = accountContextService.getContextAuthorities();

        board.validateAccessAuthority(authorities);
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
        Set<String> authorities = accountContextService.getContextAuthorities();
        board.validateAccessAuthority(authorities);

        Page<Post> allByBoard = postJpaRepository.findAllByBoard(board, pageable);
        List<PostReadDto.ListResponse> list = allByBoard.stream()
                .map(p -> PostReadDto.ListResponse.fromEntity(p))
                .collect(Collectors.toList());
        return new PageImpl<>(list, allByBoard.getPageable(), allByBoard.getTotalElements());
    }

    private boolean isOwnerOrHasManageAuthority(Post post) {
        Set<String> authorities = accountContextService.getContextAuthorities();
        if(post.getAccount() == null)
            return post.getIsSecret() == PostIsSecret.NORMAL;
        if(authorities.contains(Post.MANAGE_AUTHORITY) || post.isOwner(accountContextService.getContextAccount()))
            return true;
        return false;
    }

    public PageImpl search(PostReadDto.SearchRequest pageRequest){
        Board board = boardJpaRepository.findById(pageRequest.getBoardId())
            .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        Set<String> authorities = accountContextService.getContextAuthorities();
        board.validateAccessAuthority(authorities);

        Page<Post> postPage = postQueryRepository.search(pageRequest);
        List<PostReadDto.ListResponse> res = postPage.get().map(post -> PostReadDto.ListResponse.fromEntity(post))
            .collect(Collectors.toList());
        return new PageImpl(res, postPage.getPageable(), postPage.getTotalElements());
    }
}
