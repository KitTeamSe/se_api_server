package com.se.apiserver.v1.participatedteacher.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipatedTeacher extends AccountGenerateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long participatedTeacherId;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "time_table_id", referencedColumnName = "timeTableId", nullable = false)
  private TimeTable timeTable;


  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId", nullable = false)
  private Teacher teacher;

  @Builder
  public ParticipatedTeacher(Long participatedTeacherId,
      Teacher teacher, TimeTable timeTable) {
    this.participatedTeacherId = participatedTeacherId;
    this.timeTable = timeTable;
    this.teacher = teacher;
  }
}
