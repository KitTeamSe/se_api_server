package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDeleteService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AttachDeleteService {

  private final MultipartFileDeleteService multipartFileDeleteService;
  private final AttachJpaRepository attachJpaRepository;

  public AttachDeleteService(
      MultipartFileDeleteService multipartFileDeleteService,
      AttachJpaRepository attachJpaRepository) {
    this.multipartFileDeleteService = multipartFileDeleteService;
    this.attachJpaRepository = attachJpaRepository;
  }

  @Transactional
  public boolean delete(Long id) {
    Attach attach = attachJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));

    try {
      multipartFileDeleteService.delete(attach.getSaveName());
    } catch (BusinessException e) {
      String errorCode = e.getErrorCode().getCode();
      // 여기서 발생하는 에러는 파일 서버에서 전달된 에러일 확률이 높음.
      // 발생하는 에러는 로깅만 해주고, API 서버에서는 삭제된 것으로 처리하면 될듯.
    }

    attach.remove();
    attachJpaRepository.delete(attach);
    return true;
  }

  @Transactional
  public boolean deleteAllByOwnerId(Long postId, Long replyId) {
    validateInvalidInput(postId, replyId);

    if (postId != null) {
      List<Attach> attachList = attachJpaRepository.findAllByPostId(postId);
      String[] saveNames = createSaveNamesToDeleteRealAttaches(attachList);
      attachJpaRepository.deleteAttachesByPostId(postId);
//    multipartFileDeleteService.delete(saveNames);
    }

    if (replyId != null) {
      List<Attach> attachList = attachJpaRepository.findAllByReplyId(replyId);
      String[] saveNames = createSaveNamesToDeleteRealAttaches(attachList);
      attachJpaRepository.deleteAttachesByReplyId(replyId);
//    multipartFileDeleteService.delete(saveNames);
    }

    return true;
  }

  private void validateInvalidInput(Long postId, Long replyId) {
    if ((postId == null) == (replyId == null)) {
      throw new BusinessException(AttachErrorCode.INVALID_INPUT);
    }
  }

  private String[] createSaveNamesToDeleteRealAttaches(List<Attach> attachList) {
    String[] saveNames = new String[attachList.size()];

    for (Attach attach : attachList) {
      saveNames[attachList.indexOf(attach)] = attach.getSaveName();
      attach.remove();
    }

    return saveNames;
  }
}
