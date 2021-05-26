package com.se.apiserver.v1.lectureroom.infra.repository;

import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import java.util.Optional;

public interface LectureRoomQueryRepository {
  Optional<LectureRoom> findByRoomNumberWithBuilding(String building, String roomNumber);
}
