package com.se.apiserver.v1.opensubject.infra.repository;

import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenSubjectJpaRepository extends JpaRepository<OpenSubject, Long> {

  @Override
  Optional<OpenSubject> findById(Long openSubjectId);

  Optional<OpenSubject> findByTimeTableAndSubject(TimeTable timeTable, Subject subject);

  Page<OpenSubject> findAllByTimeTable(Pageable pageable, TimeTable timeTable);
}
