package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.application.dto.AttachReadDto.Request;
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

  private static final int CREATE_FILES = 1;
  private static final int SET_FILES_OWNER = 2;

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
    validateInvalidInput(postId, replyId, CREATE_FILES);
    Attach attach = getAttach(postId, replyId, multipartFile);
    Attach save = attachJpaRepository.save(attach);
    return Response.fromEntity(save);
  }

  @Transactional
  public List<AttachReadDto.Response> createFiles(Long postId, Long replyId,
      MultipartFile[] multipartFiles) {
    validateInvalidInput(postId, replyId, CREATE_FILES);

    List<Attach> attachList = new ArrayList<>();
    for (MultipartFile file : multipartFiles) {
      attachList.add(getAttach(postId, replyId, file));
    }

    return attachJpaRepository.saveAll(attachList)
        .stream()
        .map(attach -> new Response(attach.getAttachId(), postId, replyId,
            attach.getDownloadUrl(), attach.getFileName()))
        .collect(Collectors.toList());
  }

  public void setFileOwner(Long postId, Long replyId, List<AttachReadDto.Request> requestList) {
    validateInvalidInput(postId, replyId, SET_FILES_OWNER);

    List<Long> attachIdVals = requestList
        .stream()
        .map(Request::getAttachId)
        .collect(Collectors.toList());
    List<Attach> attachList = attachJpaRepository.findAllByAttachId(attachIdVals);

    if (postId != null) {
      Post post = postJpaRepository.findById(postId)
          .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
      for (Attach attach : attachList) {
        attach.setPost(post);
      }
    } else {
      Reply reply = replyJpaRepository.findById(replyId)
          .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
      for (Attach attach : attachList) {
        attach.setReply(reply);
      }
    }

    attachJpaRepository.saveAll(attachList);
  }

  private void validateInvalidInput(Long postId, Long replyId, int type) {
    switch(type) {
      case CREATE_FILES:
        if (postId != null && replyId != null) {
          throw new BusinessException(AttachErrorCode.INVALID_INPUT);
        }
        break;
      case SET_FILES_OWNER:
        if ((postId != null && replyId != null) || (postId == null && replyId == null)) {
          throw new BusinessException(AttachErrorCode.INVALID_INPUT);
        }
        break;
    }
  }

  private Attach getAttach(Long postId, Long replyId, MultipartFile multipartFile) {
    String fileOriginalName = multipartFile.getOriginalFilename();
    if (postId != null) {
      Post post = postJpaRepository.findById(postId)
          .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
      return new Attach(upload(multipartFile).getFileDownloadUrl(), fileOriginalName, post);
    }

    if (replyId != null) {
      Reply reply = replyJpaRepository.findById(replyId)
          .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
      return new Attach("tempURL", fileOriginalName, reply);
//      return new Attach(upload(multipartFile).getFileDownloadUrl(), fileOriginalName, reply);
    }

    return new Attach("tempURL", fileOriginalName);
//    return new Attach(upload(multipartFile).getFileDownloadUrl(), fileOriginalName);
  }

  private MultipartFileUploadDto.Response upload(MultipartFile multipartFile) {
    return multipartFileUploadService.upload(multipartFile);
  }
}
