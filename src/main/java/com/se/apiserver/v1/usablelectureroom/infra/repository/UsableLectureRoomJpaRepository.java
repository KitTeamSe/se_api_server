package com.se.apiserver.v1.usablelectureroom.infra.repository;

import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsableLectureRoomJpaRepository extends JpaRepository<UsableLectureRoom, Long> {

  @Override
  Optional<UsableLectureRoom> findById(Long usableLectureRoomId);

  Optional<UsableLectureRoom> findByTimeTableAndLectureRoom(TimeTable timeTable, LectureRoom lectureRoom);

  Page<UsableLectureRoom> findAllByTimeTable(Pageable pageable, TimeTable timeTable);
}
