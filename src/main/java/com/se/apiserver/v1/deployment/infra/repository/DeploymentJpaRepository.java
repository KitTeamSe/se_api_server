package com.se.apiserver.v1.deployment.infra.repository;

import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentJpaRepository extends JpaRepository<Deployment, Long> {

  @Override
  Optional<Deployment> findById(Long deploymentId);

  List<Deployment> findAllByTimeTableAndOpenSubjectAndDivision(TimeTable timeTable, OpenSubject openSubject, Integer division);

  List<Deployment> findAllByTimeTable(TimeTable timeTable);

  List<Deployment> findAllByTimeTableAndDayOfWeek(TimeTable timeTable, DayOfWeek dayOfWeek);
}
