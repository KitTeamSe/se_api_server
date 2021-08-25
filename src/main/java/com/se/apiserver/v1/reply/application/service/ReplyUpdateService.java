package com.se.apiserver.v1.reply.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.attach.application.service.AttachUpdateService;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.dto.ReplyUpdateDto;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ReplyUpdateService {

  private ReplyJpaRepository replyJpaRepository;
  private PostJpaRepository postJpaRepository;
  private AccountContextService accountContextService;
  private PasswordEncoder passwordEncoder;
  private AttachJpaRepository attachJpaRepository;
  private AttachUpdateService attachUpdateService;

  public ReplyUpdateService(
      ReplyJpaRepository replyJpaRepository,
      PostJpaRepository postJpaRepository,
      AccountContextService accountContextService,
      PasswordEncoder passwordEncoder,
      AttachJpaRepository attachJpaRepository,
      AttachUpdateService attachUpdateService) {
    this.replyJpaRepository = replyJpaRepository;
    this.postJpaRepository = postJpaRepository;
    this.accountContextService = accountContextService;
    this.passwordEncoder = passwordEncoder;
    this.attachJpaRepository = attachJpaRepository;
    this.attachUpdateService = attachUpdateService;
  }

  @Transactional
  public Long update(ReplyUpdateDto.Request request, MultipartFile[] files) {
    Reply reply = replyJpaRepository.findById(request.getReplyId())
        .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    Post post = postJpaRepository.findById(request.getPostId())
        .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));

    post.validateReadable();
    post.getBoard().validateAccessAuthority(accountContextService.getContextAuthorities());

    attachUpdateService.update(null, reply.getReplyId(), files);
    List<Attach> attaches = attachJpaRepository.findAllByReplyId(reply.getReplyId());

    if (reply.getAnonymous() != null) {
      validatePasswordMatch(reply, request.getPassword());
    } else {
      validateIsWriter(reply);
    }

    updateTextIfNotNull(reply, request.getText());
    updateReplyIsSecretIfNotNull(reply, request.getIsSecret());
    updateLastModifiedIp(reply, accountContextService.getCurrentClientIP());

    reply = replyJpaRepository.save(reply);
    reply.updateAttaches(attaches);

    return reply.getReplyId();
  }

  private void updateLastModifiedIp(Reply reply, String currentClientIP) {
    reply.updateLastModifiedIp(currentClientIP);
  }

  private void updateReplyIsSecretIfNotNull(Reply reply, ReplyIsSecret isSecret) {
    if (isSecret == null) {
      return;
    }
    reply.updateIsSecret(isSecret);
  }

  private void updateTextIfNotNull(Reply reply, String text) {
    if (text == null || text.isBlank()) {
      return;
    }
    reply.updateText(text);
  }

  private void validateIsWriter(Reply reply) throws AccessDeniedException {
    if (!reply.isOwner(accountContextService.getCurrentAccountId())) {
      throw new AccessDeniedException("작성자 본인만 삭제 가능합니다");
    }
  }

  private void validatePasswordMatch(Reply reply, String password) {
    if (!passwordEncoder.matches(password, reply.getAnonymous().getAnonymousPassword())) {
      throw new BusinessException(ReplyErrorCode.INVALID_PASSWORD);
    }
  }
}
