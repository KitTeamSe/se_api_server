package com.se.apiserver.v1.deployment.infra.repository;

import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentJpaRepository extends JpaRepository<Deployment, Long> {

  @Override
  Optional<Deployment> findById(Long deploymentId);

  List<Deployment> findAllByTimeTableAndDivision(TimeTable timeTable, Division division);

  Page<Deployment> findAllByTimeTable(Pageable pageable, TimeTable timeTable);

  List<Deployment> findAllByTimeTableAndDayOfWeek(TimeTable timeTable, DayOfWeek dayOfWeek);
}
