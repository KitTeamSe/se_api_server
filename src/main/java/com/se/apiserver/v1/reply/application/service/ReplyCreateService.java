package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.application.service.AttachCreateService;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ReplyCreateService {

  private final ReplyJpaRepository replyJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final AccountContextService accountContextService;
  private final AttachCreateService attachCreateService;
  private final AttachJpaRepository attachJpaRepository;
  private final PasswordEncoder passwordEncoder;

  public ReplyCreateService(
      ReplyJpaRepository replyJpaRepository,
      PostJpaRepository postJpaRepository,
      AccountContextService accountContextService,
      AttachCreateService attachCreateService,
      AttachJpaRepository attachJpaRepository,
      PasswordEncoder passwordEncoder) {
    this.replyJpaRepository = replyJpaRepository;
    this.postJpaRepository = postJpaRepository;
    this.accountContextService = accountContextService;
    this.attachCreateService = attachCreateService;
    this.attachJpaRepository = attachJpaRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public Long create(ReplyCreateDto.Request request, MultipartFile[] files) {
    Post post = postJpaRepository.findById(request.getPostId())
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));

    Set<String> authorities = accountContextService.getContextAuthorities();
    post.validateReadable();
    post.getBoard().validateAccessAuthority(authorities);

    Reply parent = getParent(request.getParentId());
    String ip = accountContextService.getCurrentClientIP();

    Reply reply;

    if (accountContextService.isSignIn()) {
      Account account = accountContextService.getContextAccount();
      reply = new Reply(post, request.getText(), request.getIsSecret(), new ArrayList<>(), parent,
          ip, account);
    } else {
      request.getAnonymous().setAnonymousPassword(
          passwordEncoder.encode(request.getAnonymous().getAnonymousPassword()));
      reply = new Reply(post, request.getText(), request.getIsSecret(), new ArrayList<>(), parent, ip,
          request.getAnonymous());
    }

    replyJpaRepository.save(reply);
    createAttaches(reply, files);

    return reply.getReplyId();
  }

  private void createAttaches(Reply reply, MultipartFile[] files) {
    if (files != null) {
      List<Long> attachIdList = attachCreateService.createAttaches(null, reply.getReplyId(), files);
      reply.updateAttaches(attachJpaRepository.findAllByAttachId(attachIdList));
    }
  }

  private Reply getParent(Long parentId) {
    if (parentId == null) {
      return null;
    }

    Reply parent = replyJpaRepository.findById(parentId)
        .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    return parent;
  }
}
