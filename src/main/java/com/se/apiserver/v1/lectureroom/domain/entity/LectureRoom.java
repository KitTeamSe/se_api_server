package com.se.apiserver.v1.lectureroom.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Getter;

@Entity
@Getter
public class LectureRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lectureRoomId;

  @Size(min = 1, max = 30)
  @Column(nullable = false)
  private String building;

  @Column(nullable = false)
  private Integer roomNumber;

  @Column(nullable = false)
  private Integer capacity;

}
