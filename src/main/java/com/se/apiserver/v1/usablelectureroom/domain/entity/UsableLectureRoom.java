package com.se.apiserver.v1.usablelectureroom.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsableLectureRoom extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long usableLectureRoomId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "time_table_id", referencedColumnName = "timeTableId", nullable = false)
  private TimeTable timeTable;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_room_id", referencedColumnName = "lectureRoomId", nullable = false)
  private LectureRoom lectureRoom;

  public UsableLectureRoom(TimeTable timeTable, LectureRoom lectureRoom) {
    this.timeTable = timeTable;
    this.lectureRoom = lectureRoom;
  }

  public void updateTimeTable(TimeTable timeTable){
    this.timeTable = timeTable;
  }
  public void updateLectureRoom(LectureRoom lectureRoom){
    this.lectureRoom = lectureRoom;
  }
}
