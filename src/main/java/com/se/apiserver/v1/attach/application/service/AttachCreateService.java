package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto.Response;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class AttachCreateService {

  private final MultipartFileUploadService multipartFileUploadService;
  private final AttachJpaRepository attachJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final ReplyJpaRepository replyJpaRepository;

  public AttachCreateService(
      MultipartFileUploadService multipartFileUploadService,
      AttachJpaRepository attachJpaRepository,
      PostJpaRepository postJpaRepository,
      ReplyJpaRepository replyJpaRepository) {
    this.multipartFileUploadService = multipartFileUploadService;
    this.attachJpaRepository = attachJpaRepository;
    this.postJpaRepository = postJpaRepository;
    this.replyJpaRepository = replyJpaRepository;
  }

  @Transactional
  public List<AttachReadDto.Response> create(Long postId, Long replyId, MultipartFile... files) {
    validateInvalidInput(postId, replyId);
    Post post = null;
    Reply reply = null;

    if (postId != null) {
      post = postJpaRepository.findById(postId)
          .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    } else if (replyId != null) {
      reply = replyJpaRepository.findById(replyId)
          .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    }

    List<Attach> attaches = getAttaches(post, reply, files);
    return attachJpaRepository.saveAll(attaches)
        .stream()
        .map(Response::fromEntity)
        .collect(Collectors.toList());
  }

  private void validateInvalidInput(Long postId, Long replyId) {
    if (postId != null && replyId != null) {
      throw new BusinessException(AttachErrorCode.INVALID_INPUT);
    }
  }

  private List<Attach> getAttaches(Post post, Reply reply, MultipartFile... files) {
    List<MultipartFileUploadDto> multipartFileUploadDtoList = upload(files);
    List<Attach> attaches = new ArrayList<>();

    for (MultipartFileUploadDto multipartFileUploadDto : multipartFileUploadDtoList) {
      Attach attach
          = new Attach(multipartFileUploadDto.getDownloadUrl(), multipartFileUploadDto.getOriginalName());
      if (post != null) {
        attach.setPost(post);
      } else if (reply != null) {
        attach.setReply(reply);
      }
      attaches.add(attach);
    }

    return attaches;
  }

  private List<MultipartFileUploadDto> upload(MultipartFile... files) {
    return multipartFileUploadService.upload(files);
  }
}
