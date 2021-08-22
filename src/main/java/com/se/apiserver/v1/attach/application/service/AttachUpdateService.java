package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDeleteService;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachUpdateService {

  private final PostJpaRepository postJpaRepository;
  private final ReplyJpaRepository replyJpaRepository;
  private final MultipartFileUploadService multipartFileUploadService;
  private final MultipartFileDeleteService multipartFileDeleteService;
  private final AttachJpaRepository attachJpaRepository;

  public AttachUpdateService(
      PostJpaRepository postJpaRepository,
      ReplyJpaRepository replyJpaRepository,
      MultipartFileUploadService multipartFileUploadService,
      MultipartFileDeleteService multipartFileDeleteService,
      AttachJpaRepository attachJpaRepository) {
    this.postJpaRepository = postJpaRepository;
    this.replyJpaRepository = replyJpaRepository;
    this.multipartFileUploadService = multipartFileUploadService;
    this.multipartFileDeleteService = multipartFileDeleteService;
    this.attachJpaRepository = attachJpaRepository;
  }

  @Transactional
  public void update(Long postId, Long replyId, MultipartFile[] files) {
    // 유효성 검사
    validateInvalidInput(postId, replyId);

    List<Attach> attachesToDelete = getAttachesToDelete(postId, replyId);
    deleteAttaches(postId, replyId);
    Post post = null;
    Reply reply = null;

    if (postId != null) {
      post = postJpaRepository.findById(postId)
          .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
    } else if (replyId != null) {
      reply = replyJpaRepository.findById(replyId)
          .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
    }

    createAttachesAndFiles(post, reply, files);
    deleteFiles(attachesToDelete);
  }

  private List<Attach> getAttachesToDelete(Long postId, Long replyId) {
    if (postId != null) {
      return attachJpaRepository.findAllByPostId(postId);
    }

    return attachJpaRepository.findAllByReplyId(replyId);
  }

  private void validateInvalidInput(Long postId, Long replyId) {
    if ((postId == null) == (replyId == null)) {
      throw new BusinessException(AttachErrorCode.INVALID_INPUT);
    }
  }

  private void createAttachesAndFiles(Post post, Reply reply, MultipartFile... files) {
    if (files == null) {
      return;
    }

    List<MultipartFileUploadDto> multipartFileUploadDtoList = upload(files);
    List<Attach> attaches = new ArrayList<>();

    for (MultipartFileUploadDto multipartFileUploadDto : multipartFileUploadDtoList) {
      if (post != null) {
        attaches.add(new Attach(multipartFileUploadDto.getDownloadUrl(),
            multipartFileUploadDto.getOriginalName(), post));
      } else {
        attaches.add(new Attach(multipartFileUploadDto.getDownloadUrl(),
            multipartFileUploadDto.getOriginalName(), reply));
      }
    }

    attachJpaRepository.saveAll(attaches);
  }

  private void deleteAttaches(Long postId, Long replyId) {
    if (postId != null) {
      attachJpaRepository.deleteAttachesByPostId(postId);
    } else {
      attachJpaRepository.deleteAttachesByReplyId(replyId);
    }
  }

  private void deleteFiles(List<Attach> attaches) {
    for (Attach attach : attaches) {
      multipartFileDeleteService.delete(attach.getSaveName());
    }
  }

  private List<MultipartFileUploadDto> upload(MultipartFile... files) {
    return multipartFileUploadService.upload(files);
  }
}
