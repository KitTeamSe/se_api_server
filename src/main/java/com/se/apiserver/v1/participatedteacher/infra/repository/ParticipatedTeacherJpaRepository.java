package com.se.apiserver.v1.participatedteacher.infra.repository;

import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipatedTeacherJpaRepository extends JpaRepository<ParticipatedTeacher, Long> {

  @Override
  Optional<ParticipatedTeacher> findById(Long participatedTeacherId);

  @Query("select pt " +
          "from ParticipatedTeacher pt " +
          "where pt.teacher.teacherId = :teacherId and pt.timeTable.timeTableId = :timeTableId")
  Optional<ParticipatedTeacher> findByTeacherIdAndTimeTableId(Long teacherId, Long timeTableId);


  Page<ParticipatedTeacher> findAllByTimeTable(Pageable pageable, TimeTable timeTable);
}
