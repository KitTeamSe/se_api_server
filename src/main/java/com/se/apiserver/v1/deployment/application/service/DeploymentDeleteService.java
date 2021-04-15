package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.error.DeploymentErrorCode;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeploymentDeleteService {

  private final DeploymentJpaRepository deploymentJpaRepository;
  private final DivisionJpaRepository divisionJpaRepository;

  @Transactional
  public void delete(Long usableLectureRoomId){
    Deployment deployment = deploymentJpaRepository
        .findById(usableLectureRoomId)
        .orElseThrow(() ->
            new BusinessException(DeploymentErrorCode.NO_SUCH_DEPLOYMENT)
        );

    Division division = deployment.getDivision();
    PeriodRange periodRange = deployment.getPeriodRange();

    updateDeployedTeachingTime(division, division.getDeployedTeachingTime() - periodRange.getTeachingTime());

    deploymentJpaRepository.delete(deployment);
  }

  private void updateDeployedTeachingTime(Division division, Integer newTeachingTime){
    division.updateDeployedTeachingTime(newTeachingTime);
    divisionJpaRepository.save(division);
  }
}
