package com.se.apiserver.v1.lectureunabletime.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Getter;

@Entity
@Getter
public class LectureUnableTime extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lectureUnableTimeId;

  // Foreign Key
  @Column(nullable = false)
  private Long teacherId;

  @Size(min = 4, max = 20)
  @Column(nullable = false)
  private DayOfWeek dayOfWeek;

  @Column(nullable = false)
  private Long startPeriodId;

  @Column(nullable = false)
  private Long endPeriodId;

  @Size(min = 2, max = 255)
  @Column(nullable = true)
  private String note;
}
