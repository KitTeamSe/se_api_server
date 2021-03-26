package com.se.apiserver.v1.teacher.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
public class ParticipatedTeacher extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long participatedTeacherId;

  @Column(nullable = false)
  private Long teacherId;

  @Column(nullable = false)
  private Long timeTableId;

}
