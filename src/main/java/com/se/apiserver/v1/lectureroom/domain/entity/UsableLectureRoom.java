package com.se.apiserver.v1.lectureroom.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class UsableLectureRoom extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountId;

  @Column(nullable = false)
  private Long timeTableId;

  @Column(nullable = false)
  private Long lectureRoomId;
}
