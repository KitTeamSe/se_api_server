package com.se.apiserver.v1.lectureroom.infra.repository;

import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LectureRoomJpaRepository extends JpaRepository<LectureRoom, Long> {
  @Override
  Optional<LectureRoom> findById(Long lectureRoomId);

  Optional<LectureRoom> findByBuilding(String building);

  Optional<LectureRoom> findByRoomNumber(Integer roomNumber);
}
