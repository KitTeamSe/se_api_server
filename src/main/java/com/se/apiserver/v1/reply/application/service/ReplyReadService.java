package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyReadDto;
import com.se.apiserver.v1.reply.application.dto.ReplyReadDto.Response;
import com.se.apiserver.v1.reply.application.dto.ReplyReadDto.ResponseListWithPage;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReplyReadService {

  private final ReplyJpaRepository replyJpaRepository;
  private final AccountContextService accountContextService;
  private final PostJpaRepository postJpaRepository;
  private final PasswordEncoder passwordEncoder;

  public ReplyReadService(
      ReplyJpaRepository replyJpaRepository,
      AccountContextService accountContextService,
      PostJpaRepository postJpaRepository,
      PasswordEncoder passwordEncoder) {
    this.replyJpaRepository = replyJpaRepository;
    this.accountContextService = accountContextService;
    this.postJpaRepository = postJpaRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // 관리자는 삭제 된것도 읽을 수 있어야함, 나머지는 대치된 값으로 조회
  // 비밀 댓글은 본인, 관리자, 게시글 작성자만 볼 수 있음.
  public ReplyReadDto.Response read(Long replyId) {
    Reply reply = replyJpaRepository.findById(replyId)
        .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    Set<String> authorities = accountContextService.getContextAuthorities();

    return ReplyReadDto.Response.fromEntity(reply
        , Reply.hasManageAuthority(authorities)
        , accountContextService.isSignIn()
            && reply.hasAccessAuthority(accountContextService.getCurrentAccountId()));
  }

  public ReplyReadDto.Response readAnonymousSecretReply(Long replyId, String password) {
    Reply reply = replyJpaRepository.findById(replyId)
        .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    validateAnonymousPostPassword(reply, password);
    Set<String> authorities = accountContextService.getContextAuthorities();

    return ReplyReadDto.Response.fromEntity(reply, Reply.hasManageAuthority(authorities), true);
  }

  private void validateAnonymousPostPassword(Reply reply, String password) {
    if (!passwordEncoder.matches(password, reply.getAnonymousPassword())) {
      throw new BusinessException(ReplyErrorCode.INVALID_PASSWORD);
    }
  }

  public ReplyReadDto.ResponseListWithPage readAllBelongPost(Long postId, Pageable pageable) {
    Post post = postJpaRepository.findById(postId)
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    Set<String> authorities = accountContextService.getContextAuthorities();
    post.validateBoardAccessAuthority(authorities);
    post.validateReadable();

    List<Reply> replies = replyJpaRepository.findAllBelongPost(post);

    boolean isSignIn = accountContextService.isSignIn();
    Long currentAccountId = accountContextService.getCurrentAccountId();
    Boolean hasManageAuthority = Reply.hasManageAuthority(authorities);

    List<Response> responseList = new ArrayList<>();
    int startPos = pageable.getPageSize() * pageable.getPageNumber() + 1;
    int endPos = startPos + pageable.getPageSize();
    int count = 0;

    for (Reply parent : replies) {
      count++;
      if (count >= endPos) {
        continue;
      }

      Response response;
      if (count >= startPos) {
        response = Response.fromEntity(parent, hasManageAuthority,
            isSignIn && parent.hasAccessAuthority(currentAccountId));
      } else {
        // 페이지 첫 댓글이 최상위 댓글이 아닐 경우
        response = new Response();
      }

      for (Reply child : parent.getChild()) {
        count++;
        if (count >= endPos) {
          continue;
        }
        if (count >= startPos) {
          response.addChild(Response.fromEntity(child, hasManageAuthority,
              isSignIn && child.hasAccessAuthority(currentAccountId)));
        }
      }

      // 1페이지 제외 첫 번째 댓글이 앞 댓글에 이어지는 대댓글에 대비
      if (response.getPostId() != null || response.getChild() != null) {
        if (response.getChild() != null) {
          Collections.sort(response.getChild());
        }
        responseList.add(response);
      }
    }

    return createResponsesWithPageInfo(responseList, count, pageable.getPageSize(),
        pageable.getPageNumber(), endPos);
  }

  private ResponseListWithPage createResponsesWithPageInfo(List<Response> responseList,
      int total, int pageSize, int currentPage, int endPos) {
    int totalPage = total % pageSize == 0 ?
        total / pageSize : total / pageSize + 1;
    int perPage = totalPage - currentPage > 1 ? pageSize : endPos - total - 1;

    if (totalPage - currentPage < 1) {
      total = totalPage = currentPage = perPage = -1;
    }

    return ReplyReadDto.ResponseListWithPage.builder()
        .responseList(responseList)
        .totalData(total)
        .totalPage(totalPage)
        .currentPage(currentPage)
        .perPage(perPage)
        .build();
  }
}
