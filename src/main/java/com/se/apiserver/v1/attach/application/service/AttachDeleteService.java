package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.application.error.AttachErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachDeleteService {

  private final AttachJpaRepository attachJpaRepository;

  public boolean delete(Long id) {
    Attach attach = attachJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));
    attach.remove();
    attachJpaRepository.delete(attach);
    return true;
  }

}
