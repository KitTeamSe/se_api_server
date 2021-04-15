package com.se.apiserver.v1.teacher.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teacherId;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false, length = 30)
  @Enumerated(EnumType.STRING)
  private TeacherType type;

  @Size(min = 2, max = 30)
  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private Boolean autoCreated;

  @Size(max = 255)
  private String note;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "teacher", orphanRemoval = true)
  private List<ParticipatedTeacher> participatedTeachers;

  public Teacher(@Size(min = 2, max = 20) String name, TeacherType type,
      @Size(min = 2, max = 30) String department, Boolean autoCreated) {
    this.name = name;
    this.type = type;
    this.department = department;
    this.autoCreated = autoCreated;
  }

  public Teacher(String name, TeacherType type, String department, Boolean autoCreated, @Size(max = 255) String note) {
    this(name, type, department, autoCreated);
    this.note = note;
  }

  public Teacher(String name, TeacherType type, String department,
      Boolean autoCreated, @Size(max = 255) String note, List<ParticipatedTeacher> participatedTeachers) {
    this(name, type, department, autoCreated);
    this.note = note;
    addParticipatedTeachers(participatedTeachers);
  }

  public void updateName(String name){
    this.name = name;
  }

  public void updateType(TeacherType type){
    this.type = type;
  }

  public void updateDepartment(String department){
    this.department = department;
  }

  public void updateNote(String note){
    this.note = note;
  }

  private void addParticipatedTeachers(List<ParticipatedTeacher> participatedTeachers){
    participatedTeachers.forEach((pt) -> pt.updateTeacher(this));
  }
}
