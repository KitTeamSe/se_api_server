package com.se.apiserver.v1.lectureunabletime.infra.repository;

import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureUnableTimeJpaRepository extends JpaRepository<LectureUnableTime, Long> {

  @Override
  Optional<LectureUnableTime> findById(Long id);

  Page<LectureUnableTime> findAllByParticipatedTeacher(Pageable pageable, ParticipatedTeacher participatedTeacher);

}
