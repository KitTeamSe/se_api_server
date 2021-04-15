package com.se.apiserver.v1.timetable.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "timeTable", orphanRemoval = true)
  private List<ParticipatedTeacher> participatedTeachers = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "timeTable", orphanRemoval = true)
  private List<UsableLectureRoom> usableLectureRooms = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "timeTable", orphanRemoval = true)
  private List<OpenSubject> openSubjects = new ArrayList<>();


  public TimeTable(@Size(min = 1, max = 50) String name, Integer year, Integer semester, TimeTableStatus status) {
    validateYear(year);
    validateSemester(semester);

    this.name = name;
    this.year = year;
    this.semester = semester;
    this.status = status;
  }

  public TimeTable(String name, Integer year, Integer semester, TimeTableStatus status,
      List<ParticipatedTeacher> participatedTeachers,
      List<UsableLectureRoom> usableLectureRooms,
      List<OpenSubject> openSubjects){
    this(name, year, semester, status);

    addParticipatedTeachers(participatedTeachers);
    addUsableLectureRooms(usableLectureRooms);
    addOpenSubjects(openSubjects);
  }

  private void validateYear(Integer year){
    if(year <= 1900)
      throw new BusinessException(TimeTableErrorCode.INVALID_YEAR);
  }

  private void validateSemester(Integer semester){
    if(semester < 0)
      throw new BusinessException(TimeTableErrorCode.INVALID_SEMESTER);
  }

  public void updateName(String name){
    this.name = name;
  }

  public void updateYear(Integer year){
    validateYear(year);
    this.year = year;
  }

  public void updateSemester(Integer semester){
    validateSemester(semester);
    this.semester = semester;
  }

  public void updateStatus(TimeTableStatus status){
    this.status = status;
  }

  private void addParticipatedTeachers(List<ParticipatedTeacher> participatedTeachers){
    participatedTeachers.forEach((pt) -> pt.updateTimeTable(this));
  }

  private void addUsableLectureRooms(List<UsableLectureRoom> usableLectureRooms){
//    usableLectureRooms.forEach((ulr) -> ulr.updateTimeTable(this));
  }

  private void addOpenSubjects(List<OpenSubject> openSubjects){
    openSubjects.forEach((os) -> os.updateTimeTable(this));
  }
}
