package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.multipartfile.application.error.FileServerErrorCodeProxy;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachDeleteService {

  @Autowired
  private final MultipartFileDeleteService multipartFileDeleteService;

  private final AttachJpaRepository attachJpaRepository;

  @Transactional
  public boolean delete(Long id) {
    Attach attach = attachJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));

    try{
      multipartFileDeleteService.deleteByDownloadUrl(attach.getDownloadUrl());
    }
    catch (BusinessException e){
      String errorCode = e.getErrorCode().getCode();
      // 여기서 발생하는 에러는 파일 서버에서 전달된 에러일 확률이 높음.
      // 발생하는 에러는 로깅만 해주고, API 서버에서는 삭제된 것으로 처리하면 될듯.
    }

    attach.remove();
    attachJpaRepository.delete(attach);
    return true;
  }

}
