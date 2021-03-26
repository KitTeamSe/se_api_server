package com.se.apiserver.v1.placement.domain.entity;

import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Placement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long placementId;

  @Column(nullable = false)
  private Long timeTableId;

  @Column(nullable = false)
  private Long openSubjectId;

  @Column(nullable = false)
  private Long lectureRoomId;

  @Column(nullable = false)
  private Long participatedTeacherId;

  @Column(nullable = false)
  private DayOfWeek dayOfWeek;

  @Column(nullable = false)
  private Integer division;

  @Column(nullable = false)
  private Long startPeriodId;

  @Column(nullable = false)
  private Long endPeriodId;

}
