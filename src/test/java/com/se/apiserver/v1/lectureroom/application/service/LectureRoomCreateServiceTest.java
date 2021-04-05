package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomCreateDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomCreateServiceTest {

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomCreateService lectureRoomCreateService;

  @Test
  void 강의실_추가_성공(){
    // Given
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("D")
        .roomNumber(332)
        .capacity(30)
        .build();

    // When
    Long id = lectureRoomCreateService.create(req);

    // Then
    Assertions.assertThat(lectureRoomJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 강의실_추가_중복_실패(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    // When
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("D")
        .roomNumber(330)
        .capacity(50)
        .build();

    // Then
    Assertions.assertThatThrownBy(()-> lectureRoomCreateService.create(req)).isInstanceOf(BusinessException.class).hasMessage(LectureRoomErrorCode.DUPLICATED_LECTURE_ROOM.getMessage());
  }

  @Test
  void 강의실_추가_건물만_같음_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    // When
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("D")
        .roomNumber(332)
        .capacity(30)
        .build();

    Long id = lectureRoomCreateService.create(req);

    // Then
    Assertions.assertThat(lectureRoomJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 강의실_추가_호수만_같음_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    // When
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("G")
        .roomNumber(330)
        .capacity(30)
        .build();

    Long id = lectureRoomCreateService.create(req);

    // Then
    Assertions.assertThat(lectureRoomJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }


}
