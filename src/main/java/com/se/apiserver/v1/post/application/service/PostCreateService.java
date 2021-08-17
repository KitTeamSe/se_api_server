package com.se.apiserver.v1.post.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.application.service.AttachCreateService;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.notice.application.service.NoticeSendService;
import com.se.apiserver.v1.post.domain.entity.*;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCreateService {

  private final PostJpaRepository postJpaRepository;
  private final AccountContextService accountContextService;
  private final AttachCreateService attachCreateService;
  private final BoardJpaRepository boardJpaRepository;
  private final PasswordEncoder passwordEncoder;
  private final TagJpaRepository tagJpaRepository;
  private final AttachJpaRepository attachJpaRepository;
  private final NoticeSendService noticeSendService;

  @Transactional
  public Long create(PostCreateDto.Request request, MultipartFile[] files) {
    Board board = boardJpaRepository.findById(request.getBoardId())
        .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));

    Set<String> authorities = accountContextService.getContextAuthorities();
    List<Tag> tags = getTagsIfSignIn(request.getTagList());
    String ip = accountContextService.getCurrentClientIP();

    if (accountContextService.isSignIn()) {
      Account contextAccount = accountContextService.getContextAccount();
      Post post = new Post(contextAccount, board, request.getPostContent(), request.getIsNotice(),
          request.getIsSecret(), authorities, tags, new ArrayList<>(), ip);
      postJpaRepository.save(post);

      createAttaches(post, files);

      //Notice 호출
      noticeSendService.sendPostNotice(tags, post);

      return post.getPostId();
    }

    validateAnonymousInput(request);
    request.getAnonymous().setAnonymousPassword(
        passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
    Post post = new Post(request.getAnonymous(), board, request.getPostContent(),
        request.getIsNotice()
        , request.getIsSecret(), authorities, tags, new ArrayList<>(), ip);

    postJpaRepository.save(post);
    createAttaches(post, files);

    return post.getPostId();
  }

  private void createAttaches(Post post, MultipartFile[] files) {
    if (files != null) {
      attachCreateService.createAttaches(post.getPostId(), null, files);
      post.updateAttaches(attachJpaRepository.findAllByPostId(post.getPostId()));
    }
  }

  private void validateAnonymousInput(PostCreateDto.Request request) {
    if (request.getAnonymous() == null) {
      throw new BusinessException(PostErrorCode.INVALID_INPUT);
    }
  }

  // only signed user can add tags
  private List<Tag> getTagsIfSignIn(List<PostCreateDto.TagDto> tagList) {
    if (tagList == null || tagList.size() == 0) {
      return new ArrayList<>();
    }
    if (!accountContextService.isSignIn()) {
      throw new BusinessException(TagErrorCode.ANONYMOUS_CAN_NOT_TAG);
    }
    return tagList.stream()
        .map(t ->
            tagJpaRepository.findById(t.getTagId())
                .orElseThrow(() -> new BusinessException(TagErrorCode.NO_SUCH_TAG))
        )
        .collect(Collectors.toList());
  }
}
