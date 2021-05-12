package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto.Response;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachCreateService {

  @Autowired
  private final MultipartFileUploadService multipartFileUploadService;

  private final AttachJpaRepository attachJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final ReplyJpaRepository replyJpaRepository;

  @Transactional
  public AttachReadDto.Response create(Long postId, Long replyId, MultipartFile multipartFile){
    validateInvalidInput(postId, replyId);
    Attach attach = getAttach(postId, replyId, multipartFile);
    Attach save = attachJpaRepository.save(attach);
    return Response.fromEntity(save);
  }

  private void validateInvalidInput(Long postId, Long replyId) {
    if(postId != null && replyId != null)
      throw new BusinessException(AttachErrorCode.INVALID_INPUT);
  }

  private Attach getAttach(Long postId, Long replyId, MultipartFile multipartFile) {
    String fileOriginalName = multipartFile.getOriginalFilename();
    if(postId != null) {
      Post post = postJpaRepository.findById(postId)
              .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
      return new Attach(upload(multipartFile), fileOriginalName, post);
    }

    if(replyId != null) {
      Reply reply = replyJpaRepository.findById(replyId)
              .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
      return new Attach(upload(multipartFile), fileOriginalName, reply);
    }

    return new Attach(upload(multipartFile), fileOriginalName);
  }

  private String upload(MultipartFile multipartFile){
    return multipartFileUploadService.upload(multipartFile);
  }
}
