package com.se.apiserver.v1.job.infra.repository;

import com.se.apiserver.v1.job.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobJpaRepository extends JpaRepository<Job, Long> {

}
