package com.se.apiserver.v1.attach.domain.usecase;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.attach.infra.dto.AttachCreateDto;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto.Response;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.error.PostErrorCode;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachCreateUseCase {
  private final AttachJpaRepository attachJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final ReplyJpaRepository replyJpaRepository;

  public AttachReadDto.Response create(AttachCreateDto.Request request){
    if(request.getPostId() != null & request.getReplyId() != null)
        throw new BusinessException(AttachErrorCode.INVALID_INPUT);

    Attach attach = Attach.builder()
        .downloadUrl(request.getDownloadUrl())
        .fileName(request.getFileName())
        .build();

    if(request.getPostId() != null) {
      Post post = postJpaRepository.findById(request.getPostId())
          .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
      attach.updatePost(post);
    }

    if(request.getReplyId() != null) {
      Reply reply = replyJpaRepository.findById(request.getReplyId())
          .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
      attach.updateReply(reply);
    }

    Attach save = attachJpaRepository.save(attach);
    return Response.fromEntity(save);

  }

}
