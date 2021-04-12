package com.se.apiserver.v1.lectureroom.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureRoom extends AccountGenerateEntity {

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

  @Size(max = 255)
  private String note;

  @Builder
  public LectureRoom(Long lectureRoomId,
      @Size(min = 1, max = 30) String building, Integer roomNumber, Integer capacity, @Size(max=255) String note) {

    validateCapacity(capacity);

    this.lectureRoomId = lectureRoomId;
    this.building = building;
    this.roomNumber = roomNumber;
    this.capacity = capacity;
    this.note = note;
  }
  
  public void validateCapacity(Integer capacity){
    if(capacity < 0)
      throw new BusinessException(LectureRoomErrorCode.INVALID_CAPACITY);
  }

  public void updateBuilding(String building){
    this.building = building;
  }

  public void updateRoomNumber(Integer roomNumber){
    this.roomNumber = roomNumber;
  }

  public void updateCapacity(Integer capacity){
    validateCapacity(capacity);
    this.capacity = capacity;
  }

  public void updateNote(String note){
    this.note = note;
  }
}
