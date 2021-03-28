package com.se.apiserver.v1.placement.domain.entity;

import com.se.apiserver.v1.lectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.subject.domain.entity.OpenSubject;
import com.se.apiserver.v1.teacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Placement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long placementId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "time_table_id", referencedColumnName = "timeTableId", nullable = false)
  private TimeTable timeTable;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "open_subject_id", referencedColumnName = "openSubjectId", nullable = false)
  private OpenSubject openSubject;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "usable_lecture_room_id", referencedColumnName = "usableLectureRoomId", nullable = false)
  private UsableLectureRoom usableLectureRoom;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "participated_teacher_id", referencedColumnName = "participatedTeacherId", nullable = false)
  private ParticipatedTeacher participatedTeacherId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DayOfWeek dayOfWeek;

  @Column(nullable = false)
  private Integer division;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "start_period_id", referencedColumnName = "periodId", nullable = false)
  private Period startPeriod;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "end_period_id", referencedColumnName = "periodId", nullable = false)
  private Period endPeriod;

  @Builder
  public Placement(Long placementId, TimeTable timeTable,
      OpenSubject openSubject,
      UsableLectureRoom usableLectureRoom,
      ParticipatedTeacher participatedTeacherId,
      DayOfWeek dayOfWeek, Integer division,
      Period startPeriod, Period endPeriod) {
    this.placementId = placementId;
    this.timeTable = timeTable;
    this.openSubject = openSubject;
    this.usableLectureRoom = usableLectureRoom;
    this.participatedTeacherId = participatedTeacherId;
    this.dayOfWeek = dayOfWeek;
    this.division = division;
    this.startPeriod = startPeriod;
    this.endPeriod = endPeriod;
  }
}
