package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.application.dto.AttachCreateDto;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto.Response;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachCreateService {
  private final AttachJpaRepository attachJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final ReplyJpaRepository replyJpaRepository;

  @Transactional
  public AttachReadDto.Response create(AttachCreateDto.Request request){
    validateInvalidInput(request.getPostId(), request.getReplyId());
    Attach attach = getAttach(request);
    Attach save = attachJpaRepository.save(attach);
    return Response.fromEntity(save);
  }

  private void validateInvalidInput(Long postId, Long replyId) {
    if(postId != null && replyId != null)
      throw new BusinessException(AttachErrorCode.INVALID_INPUT);
  }

  private Attach getAttach(AttachCreateDto.Request request) {
    if(request.getPostId() != null) {
      Post post = postJpaRepository.findById(request.getPostId())
              .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
      return new Attach(request.getDownloadUrl(), request.getFileName(), post);
    }

    if(request.getReplyId() != null) {
      Reply reply = replyJpaRepository.findById(request.getReplyId())
              .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
      return new Attach(request.getDownloadUrl(), request.getFileName(), reply);
    }

    return new Attach(request.getDownloadUrl(), request.getFileName());
  }
}
