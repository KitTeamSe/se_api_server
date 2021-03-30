package com.se.apiserver.v1.attach.domain.usecase;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachDeleteUseCase {

  private final AttachJpaRepository attachJpaRepository;

  public boolean delete(Long id) {
    Attach attach = attachJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));
    attachJpaRepository.delete(attach);
    return true;
  }

}
