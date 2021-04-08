package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.error.DeploymentErrorCode;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeploymentDeleteService {

  private final DeploymentJpaRepository deploymentJpaRepository;

  @Transactional
  public void delete(Long usableLectureRoomId){
    Deployment deployment = deploymentJpaRepository
        .findById(usableLectureRoomId)
        .orElseThrow(() ->
            new BusinessException(DeploymentErrorCode.NO_SUCH_DEPLOYMENT)
        );
    deploymentJpaRepository.delete(deployment);
  }
}
