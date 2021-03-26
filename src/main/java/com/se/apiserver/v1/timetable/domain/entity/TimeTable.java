package com.se.apiserver.v1.timetable.domain.entity;

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
public class TimeTable extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long timeTableId;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int year;

  @Column(nullable = false)
  private int semester;

  //Foreign Key
  @Column(nullable = false)
  private Long accountId;

  @Column(nullable = false)
  private TimeTableStatus status;
}
