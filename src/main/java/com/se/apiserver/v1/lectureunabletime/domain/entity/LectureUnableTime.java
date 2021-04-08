package com.se.apiserver.v1.lectureunabletime.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;

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
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureUnableTime extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lectureUnableTimeId;

  // 참여 교원
  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "participated_teacher_id", referencedColumnName = "participatedTeacherId", nullable = false)
  private ParticipatedTeacher participatedTeacher;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DayOfWeek dayOfWeek;

  @Embedded
  private PeriodRange periodRange;

  @Size(max = 255)
  private String note;

  @Builder
  public LectureUnableTime(Long lectureUnableTimeId,
      ParticipatedTeacher participatedTeacher,
      DayOfWeek dayOfWeek, PeriodRange periodRange, @Size(max = 255) String note) {

    this.lectureUnableTimeId = lectureUnableTimeId;
    this.participatedTeacher = participatedTeacher;
    this.dayOfWeek = dayOfWeek;
    this.periodRange = periodRange;
    this.note = note;
  }


  public void updateDayOfWeek(DayOfWeek dayOfWeek){
    this.dayOfWeek = dayOfWeek;
  }

  public void updatePeriodRange(PeriodRange periodRange){
    this.periodRange = periodRange;
  }

  public void updateNote(String note){
    this.note = note;
  }
}
