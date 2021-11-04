package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostAccessCheckDto;
import com.se.apiserver.v1.post.application.dto.PostReadDto.PostListItem;
import com.se.apiserver.v1.post.application.dto.PostReadDto.PostSearchRequest;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.application.dto.PostReadDto;
import com.se.apiserver.v1.post.domain.repository.PostRepositoryProtocol;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostQueryRepository;
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
@Transactional(readOnly = true)
public class PostReadService {

  private final PostJpaRepository postJpaRepository;
  private final AccountContextService accountContextService;
  private final PasswordEncoder passwordEncoder;
  private final BoardJpaRepository boardJpaRepository;
  private final PostQueryRepository postQueryRepository;
  private final PostRepositoryProtocol postRepositoryProtocol;

  public PostReadService(
      PostJpaRepository postJpaRepository,
      AccountContextService accountContextService,
      PasswordEncoder passwordEncoder,
      BoardJpaRepository boardJpaRepository,
      PostQueryRepository postQueryRepository,
      PostRepositoryProtocol postRepositoryProtocol) {
    this.postJpaRepository = postJpaRepository;
    this.accountContextService = accountContextService;
    this.passwordEncoder = passwordEncoder;
    this.boardJpaRepository = boardJpaRepository;
    this.postQueryRepository = postQueryRepository;
    this.postRepositoryProtocol = postRepositoryProtocol;
  }

  @Transactional
  public PostReadDto.Response read(Long postId) {
    Post post = postJpaRepository.findById(postId)
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    Board board = post.getBoard();
    post.validateReadable();
    Set<String> authorities = accountContextService.getContextAuthorities();

    board.validateAccessAuthority(authorities);

    post.increaseViews();
    postJpaRepository.save(post);

    return PostReadDto.Response.fromEntity(post, isOwnerOrHasManageAuthority(post));
  }


  @Transactional
  public PostReadDto.Response readAnonymousSecretPost(Long postId, String password) {
    Post post = postJpaRepository.findById(postId)
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    post.validateReadable();
    validateAnonymousPostPassword(post, password);
    post.increaseViews();
    postJpaRepository.save(post);
    return PostReadDto.Response.fromEntity(post, true);
  }

  public Boolean checkAnonymousPostWriteAccess(
      PostAccessCheckDto.AnonymousPostAccessCheckDto anonymousPostAccessCheckDto) {
    Post post = postJpaRepository.findById(anonymousPostAccessCheckDto.getPostId())
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    post.validateReadable();
    validateAnonymousPostPassword(post, anonymousPostAccessCheckDto.getPassword());
    return true;
  }

  public PostReadDto.PostListResponse readBoardPostList(Pageable pageable, String boardNameEng, PostIsNotice isNotice) {
    Board board = boardJpaRepository.findByNameEng(boardNameEng)
        .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
    Set<String> authorities = accountContextService.getContextAuthorities();
    board.validateAccessAuthority(authorities);

    Page<Post> allByBoard = postJpaRepository.findAllByBoardAndIsNotice(pageable, board, isNotice);
    List<PostReadDto.PostListItem> list = allByBoard.stream()
        .map(p -> PostReadDto.PostListItem.fromEntity(p))
        .collect(Collectors.toList());
    return PostReadDto.PostListResponse
        .fromEntity(new PageImpl<>(list, allByBoard.getPageable(), allByBoard.getTotalElements()),
            board);
  }

  public PostReadDto.PostListResponse search(PostSearchRequest pageRequest) {
    Board board = boardJpaRepository.findByNameEng(pageRequest.getBoardNameEng())
        .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
    Set<String> authorities = accountContextService.getContextAuthorities();
    board.validateAccessAuthority(authorities);

    Page<Post> postPage = postQueryRepository.search(pageRequest);
    List<PostReadDto.PostListItem> res = postPage.get()
        .map(PostListItem::fromEntity)
        .collect(Collectors.toList());
    return PostReadDto.PostListResponse
        .fromEntity(new PageImpl(res, postPage.getPageable(), postPage.getTotalElements()), board);
  }

  public void validatePostManageAuthority(Long postId) {
    Post post = postJpaRepository.findById(postId)
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    if (!isOwnerOrHasManageAuthority(post)) {
      throw new BusinessException(PostErrorCode.CAN_NOT_ACCESS_POST);
    }
  }

  private void validateAnonymousPostPassword(Post post, String password) {
    if(isOwnerOrHasManageAuthority(post))
      return;
    if (!passwordEncoder.matches(password, post.getAnonymousPassword())) {
      throw new BusinessException(PostErrorCode.ANONYMOUS_PASSWORD_INCORRECT);
    }
  }


  private boolean isOwnerOrHasManageAuthority(Post post) {
    Set<String> authorities = accountContextService.getContextAuthorities();
    return authorities.contains(Post.MANAGE_AUTHORITY) || post
        .isOwner(accountContextService.getCurrentAccountId());
  }


}
