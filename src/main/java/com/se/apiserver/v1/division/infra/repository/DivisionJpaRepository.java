package com.se.apiserver.v1.division.infra.repository;

import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionJpaRepository extends JpaRepository<Division, Long> {
  @Override
  Optional<Division> findById(Long divisionId);

  List<Division> findAllByOpenSubject(OpenSubject openSubject);

  Page<Division> findAllByOpenSubject(Pageable pageable, OpenSubject openSubject);

}
