package com.se.apiserver.v1.deployment.infra.repository;

import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentJpaRepository extends JpaRepository<Deployment, Long> {

  @Override
  Optional<Deployment> findById(Long deploymentId);
}
