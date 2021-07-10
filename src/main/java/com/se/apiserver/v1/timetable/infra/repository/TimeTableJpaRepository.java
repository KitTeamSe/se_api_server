package com.se.apiserver.v1.timetable.infra.repository;

import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableJpaRepository extends JpaRepository<TimeTable, Long> {

  @Override
  Optional<TimeTable> findById(Long lectureRoomId);

  Optional<TimeTable> findByName(String name);

}
