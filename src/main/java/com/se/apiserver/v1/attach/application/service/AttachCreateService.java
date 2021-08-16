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
  public AttachReadDto.Response create(Long postId, Long replyId, MultipartFile multipartFile) {
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

    Attach attach = getAttach(post, reply, multipartFile);
    Attach save = attachJpaRepository.save(attach);
    return Response.fromEntity(save);
  }

  @Transactional
  public List<Long> createAttaches(Long postId, Long replyId,
      MultipartFile[] files) {
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

    List<Attach> attachList = getAttaches(post, reply, files);

    return attachJpaRepository.saveAll(attachList)
        .stream()
        .map(Attach::getAttachId)
        .collect(Collectors.toList());
  }

  private void validateInvalidInput(Long postId, Long replyId) {
    if (postId != null && replyId != null) {
      throw new BusinessException(AttachErrorCode.INVALID_INPUT);
    }
  }

  private Attach getAttach(Post post, Reply reply, MultipartFile multipartFile) {
    String fileOriginalName = multipartFile.getOriginalFilename();
    if (post != null) {
      return new Attach("tempURL", multipartFile.getOriginalFilename(), post);
//      return new Attach(upload(multipartFile).getFileDownloadUrl(), multipartFile.getOriginalFilename(), post);
    }

    if (reply != null) {
      return new Attach("tempURL", multipartFile.getOriginalFilename(), reply);
//      return new Attach(upload(multipartFile).getFileDownloadUrl(), fileOriginalName, reply);
    }

    return new Attach("tempURL", fileOriginalName);
//    return new Attach(upload(multipartFile).getFileDownloadUrl(), fileOriginalName);
  }

  private List<Attach> getAttaches(Post post, Reply reply, MultipartFile[] files) {
    List<Attach> attaches = new ArrayList<>();

    for (MultipartFile file : files) {
      attaches.add(getAttach(post, reply, file));
    }

    return attaches;
  }

  private MultipartFileUploadDto.Response upload(MultipartFile multipartFile) {
    return multipartFileUploadService.upload(multipartFile);
  }
}
