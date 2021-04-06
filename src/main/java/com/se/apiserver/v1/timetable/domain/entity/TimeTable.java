package com.se.apiserver.v1.timetable.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeTable extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long timeTableId;

  @Size(min = 1, max = 50)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer year;

  @Column(nullable = false)
  private Integer semester;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TimeTableStatus status;

  @Builder
  public TimeTable(Long timeTableId,
      @Size(min = 1, max = 50) String name, Integer year, Integer semester, TimeTableStatus status) {
    this.timeTableId = timeTableId;
    this.name = name;
    this.year = year;
    this.semester = semester;
    this.status = status;
  }

  public void updateName(String name){
    this.name = name;
  }

  public void updateYear(Integer year){
    this.year = year;
  }

  public void updateSemester(Integer semester){
    this.semester = semester;
  }

  public void updateStatus(TimeTableStatus status){
    this.status = status;
  }
}
