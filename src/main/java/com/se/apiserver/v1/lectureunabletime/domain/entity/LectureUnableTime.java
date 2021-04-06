package com.se.apiserver.v1.lectureunabletime.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;

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
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
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

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "start_period_id", referencedColumnName = "periodId", nullable = false)
  private Period startPeriod;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "end_period_id", referencedColumnName = "periodId", nullable = false)
  private Period endPeriod;

  @Size(max = 255)
  private String note;

  @Builder
  public LectureUnableTime(Long lectureUnableTimeId,
      ParticipatedTeacher participatedTeacher,
      DayOfWeek dayOfWeek, Period startPeriod,
      Period endPeriod, @Size(max = 255) String note) {
    this.lectureUnableTimeId = lectureUnableTimeId;
    this.participatedTeacher = participatedTeacher;
    this.dayOfWeek = dayOfWeek;
    this.startPeriod = startPeriod;
    this.endPeriod = endPeriod;
    this.note = note;
  }
}
