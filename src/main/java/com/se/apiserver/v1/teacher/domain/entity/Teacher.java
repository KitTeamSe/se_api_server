package com.se.apiserver.v1.teacher.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
public class Teacher extends BaseEntity{

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

  @Builder
  public Teacher(Long teacherId,
      @Size(min = 2, max = 20) String name,
      TeacherType type,
      @Size(min = 2, max = 30) String department) {
    this.teacherId = teacherId;
    this.name = name;
    this.type = type;
    this.department = department;
  }
}
