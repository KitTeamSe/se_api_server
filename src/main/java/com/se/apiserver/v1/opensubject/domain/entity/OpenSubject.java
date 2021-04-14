package com.se.apiserver.v1.opensubject.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "openSubject", orphanRemoval = true)
  List<Division> divisions = new ArrayList<>();

  @Column(nullable = false)
  private Integer teachingTimePerWeek;

  @Column(nullable = false)
  private Boolean autoCreated;

  @Size(max = 255)
  private String note;

  @Builder
  public OpenSubject(Long openSubjectId,
      TimeTable timeTable, Subject subject, Integer numberOfDivision,
      Integer teachingTimePerWeek, Boolean autoCreated, @Size(max=255) String note) {

    if(numberOfDivision == null)
      numberOfDivision = 1;

    validateNumberOfDivision(numberOfDivision);
    validateTeachingTimePerWeek(teachingTimePerWeek);

    this.openSubjectId = openSubjectId;
    this.timeTable = timeTable;
    this.subject = subject;
    this.teachingTimePerWeek = teachingTimePerWeek;
    this.autoCreated = autoCreated;
    this.note = note;

    IntStream.range(0, numberOfDivision).forEach((i) ->
      this.divisions.add(Division.builder().openSubject(this).build()));
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

    int diff = Math.abs(divisions.size() - numberOfDivision);

    if(numberOfDivision > divisions.size()){
      IntStream.range(0, diff).forEach((i) ->
        this.divisions.add(Division.builder().openSubject(this).deployedTeachingTime(0).build()));
    }
    else{
      divisions.sort((o1, o2) -> (int) (o2.getDivisionId() - o1.getDivisionId()));
      IntStream.range(0, diff).forEach((i) -> {
        divisions.get(0).remove();
        this.divisions.remove(0);
      });
    }
  }

  public void updateTeachingTimePerWeek(Integer teachingTimePerWeek){
    validateTeachingTimePerWeek(teachingTimePerWeek);
    this.teachingTimePerWeek = teachingTimePerWeek;
  }

  public void updateNote(String note){
    this.note = note;
  }
}
