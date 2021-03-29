package com.se.apiserver.v1.subject.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
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
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class OpenSubject extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long openSubjectId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "time_table_id", referencedColumnName = "timeTableId", nullable = false)
  private TimeTable timeTableId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "subject_id", referencedColumnName = "subjectId", nullable = false)
  private Subject subject;

  @Column(nullable = false)
  private Integer numberOfDivision;

  @Column(nullable = false)
  private Integer teachingTimePerWeek;

  @Builder
  public OpenSubject(Long openSubjectId,
      TimeTable timeTableId, Subject subject, Integer numberOfDivision,
      Integer teachingTimePerWeek) {
    this.openSubjectId = openSubjectId;
    this.timeTableId = timeTableId;
    this.subject = subject;
    this.numberOfDivision = numberOfDivision;
    this.teachingTimePerWeek = teachingTimePerWeek;
  }
}
