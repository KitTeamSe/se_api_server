package com.se.apiserver.v1.teacher.infra.repository;

import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherJpaRepository extends JpaRepository<Teacher, Long> {

  @Override
  public Optional<Teacher> findById(Long teacherId);

  Optional<Teacher> findByName(String name);

  Optional<Teacher> findByDepartment(String department);
}
