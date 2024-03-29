package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto.ReplyCreateAttachDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReplyCreateService {

  private final ReplyJpaRepository replyJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final AccountContextService accountContextService;
  private final AttachJpaRepository attachJpaRepository;
  private final PasswordEncoder passwordEncoder;

  public ReplyCreateService(
      ReplyJpaRepository replyJpaRepository,
      PostJpaRepository postJpaRepository,
      AccountContextService accountContextService,
      AttachJpaRepository attachJpaRepository,
      PasswordEncoder passwordEncoder) {
    this.replyJpaRepository = replyJpaRepository;
    this.postJpaRepository = postJpaRepository;
    this.accountContextService = accountContextService;
    this.attachJpaRepository = attachJpaRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public Long create(ReplyCreateDto.Request request) {
    Post post = postJpaRepository.findById(request.getPostId())
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    post.validateReadable();

    Set<String> authorities = accountContextService.getContextAuthorities();
    post.getBoard().validateAccessAuthority(authorities);

    Reply parent = getParent(request.getParentId());
    checkParentReplyHasParent(parent);
    checkIsParentDeleted(parent);
    String ip = accountContextService.getCurrentClientIP();

    List<Attach> attaches = getAttaches(request.getAttachmentList());

    Reply reply;

    if (accountContextService.isSignIn()) {
      Account account = accountContextService.getContextAccount();
      reply = new Reply(post, request.getText(), request.getIsSecret(), attaches, parent,
          ip, account);
    } else {
      request.getAnonymous().setAnonymousPassword(
          passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
      reply = new Reply(post, request.getText(), request.getIsSecret(), attaches, parent, ip,
          request.getAnonymous());
    }

    replyJpaRepository.save(reply);
    return reply.getReplyId();
  }


  private Reply getParent(Long parentId) {
    if (parentId == null) {
      return null;
    }

    Reply parent = replyJpaRepository.findById(parentId)
        .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    return parent;
  }

  private void checkParentReplyHasParent(Reply parent) {
    if (parent != null && parent.getParent() != null) {
      throw new BusinessException(ReplyErrorCode.INVALID_REPLY);
    }
  }

  private void checkIsParentDeleted(Reply parent){
    if(parent != null && parent.getIsDelete() == ReplyIsDelete.DELETED){
      throw new BusinessException(ReplyErrorCode.CANNOT_REPLY_DELETED_PARENT);
    }
  }

  private List<Attach> getAttaches(List<ReplyCreateAttachDto> attachmentList) {
    if(attachmentList == null || attachmentList.size() == 0)
      return new ArrayList<>();
    return attachmentList.stream()
        .map(a -> attachJpaRepository.findById(a.getAttachId())
            .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH))
        )
        .collect(Collectors.toList());
  }
}
