package com.se.apiserver.v1.lectureroom.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class LectureRoom extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lectureRoomId;

  @Size(min = 1, max = 30)
  @Column(nullable = false)
  private String building;

  @Column(nullable = false)
  private String roomNumber;

  @Column(nullable = false)
  private Integer capacity;

  @Column(nullable = false)
  private Boolean autoCreated;

  @Size(max = 255)
  private String note;

  public LectureRoom(@Size(min = 1, max = 30) String building, String roomNumber, Boolean autoCreated) {
    this.building = building;
    this.roomNumber = roomNumber;
    this.autoCreated = autoCreated;
    this.capacity = 100;
  }

  public LectureRoom(@Size(min = 1, max = 30) String building, String roomNumber, Integer capacity, Boolean autoCreated) {
    this(building, roomNumber, autoCreated);
    validateCapacity(capacity);
    this.capacity = capacity;
  }

  public LectureRoom(String building, String roomNumber, Integer capacity, Boolean autoCreated, @Size(max=255) String note) {
    this(building, roomNumber, capacity, autoCreated);
    this.note = note;
  }
  
  public void validateCapacity(Integer capacity){
    if(capacity < 0)
      throw new BusinessException(LectureRoomErrorCode.INVALID_CAPACITY);
  }

  public void updateBuilding(String building){
    this.building = building;
  }

  public void updateRoomNumber(String roomNumber){
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
