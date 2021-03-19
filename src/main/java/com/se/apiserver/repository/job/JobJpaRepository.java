package com.se.apiserver.repository.job;

import com.se.apiserver.domain.entity.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobJpaRepository extends JpaRepository<Job, Long> {

}
