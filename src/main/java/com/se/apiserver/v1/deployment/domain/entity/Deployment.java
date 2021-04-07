package com.se.apiserver.v1.deployment.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Deployment extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deploymentId;

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

  @Embedded
  private PeriodRange periodRange;

  @Builder
  public Deployment(Long deploymentId, TimeTable timeTable,
      OpenSubject openSubject,
      UsableLectureRoom usableLectureRoom,
      ParticipatedTeacher participatedTeacherId,
      DayOfWeek dayOfWeek, Integer division,
      PeriodRange periodRange) {

    this.deploymentId = deploymentId;
    this.timeTable = timeTable;
    this.openSubject = openSubject;
    this.usableLectureRoom = usableLectureRoom;
    this.participatedTeacherId = participatedTeacherId;
    this.dayOfWeek = dayOfWeek;
    this.division = division;
    this.periodRange = periodRange;
  }
}
