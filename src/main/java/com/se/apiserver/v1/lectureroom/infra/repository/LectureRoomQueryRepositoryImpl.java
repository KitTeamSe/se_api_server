package com.se.apiserver.v1.lectureroom.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.domain.entity.QLectureRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class LectureRoomQueryRepositoryImpl extends QuerydslRepositorySupport implements LectureRoomQueryRepository{
  public LectureRoomQueryRepositoryImpl(){
    super(LectureRoom.class);
  }

  @Override
  public Optional<LectureRoom> findByRoomNumberWithBuilding(String building, Integer roomNumber){
    QLectureRoom qLectureRoom = QLectureRoom.lectureRoom;

    JPQLQuery query = from(qLectureRoom);

    if(building != null){
      query.where(qLectureRoom.building.eq(building));
    }

    if(roomNumber != null){
      query.where(qLectureRoom.roomNumber.eq(roomNumber));
    }

    List<LectureRoom> a = query.fetch();

    LectureRoom result = (LectureRoom)query.fetchOne();
    return Optional.ofNullable(result);
  }
}
