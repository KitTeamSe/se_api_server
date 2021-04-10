package com.se.apiserver.v1.opensubject.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.subject.domain.entity.Subject;
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
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenSubject extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long openSubjectId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "time_table_id", referencedColumnName = "timeTableId", nullable = false)
  private TimeTable timeTable;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "subject_id", referencedColumnName = "subjectId", nullable = false)
  private Subject subject;

  @Column(nullable = false)
  private Integer numberOfDivision;

  private Integer teachingTimePerWeek;

  @Column(nullable = false)
  private Boolean autoCreated;

  @Size(max = 255)
  private String note;

  @Builder
  public OpenSubject(Long openSubjectId,
      TimeTable timeTable, Subject subject, Integer numberOfDivision,
      Integer teachingTimePerWeek) {

    validateNumberOfDivision(numberOfDivision);
    validateTeachingTimePerWeek(teachingTimePerWeek);

    this.openSubjectId = openSubjectId;
    this.timeTable = timeTable;
    this.subject = subject;
    this.numberOfDivision = numberOfDivision;
    this.teachingTimePerWeek = teachingTimePerWeek;
  }

  public void validateNumberOfDivision(Integer numberOfDivision){
    if(numberOfDivision <= 0)
      throw new BusinessException(OpenSubjectErrorCode.INVALID_NUMBER_OF_DIVISION);
  }

  public void validateTeachingTimePerWeek(Integer teachingTimePerWeek){
    if(teachingTimePerWeek <= 0)
      throw new BusinessException(OpenSubjectErrorCode.INVALID_TEACHING_TIME_PER_WEEK);
  }

  public void updateNumberOfDivision(Integer numberOfDivision){
    validateNumberOfDivision(numberOfDivision);
    this.numberOfDivision = numberOfDivision;
  }

  public void updateTeachingTimePerWeek(Integer teachingTimePerWeek){
    validateTeachingTimePerWeek(teachingTimePerWeek);
    this.teachingTimePerWeek = teachingTimePerWeek;
  }
}
