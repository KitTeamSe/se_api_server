package com.se.apiserver.v1.subject.infra.repository;

import com.se.apiserver.v1.subject.domain.entity.Subject;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectJpaRepository extends JpaRepository<Subject, Long> {

  @Override
  Optional<Subject> findById(Long id);

}
