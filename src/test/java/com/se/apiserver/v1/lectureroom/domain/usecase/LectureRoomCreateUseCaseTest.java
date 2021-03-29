package com.se.apiserver.v1.lectureroom.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.domain.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomCreateDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomQueryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomCreateUseCaseTest {
  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomQueryRepository lectureRoomQueryRepository;

  @Autowired
  LectureRoomCreateUseCase lectureRoomCreateUseCase;

  @Test
  void 강의실_추가_성공(){
    // Given
    LectureRoom lectureRoom = LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build();

    // When
    lectureRoomJpaRepository.save(lectureRoom);

    // Then
    Assertions.assertThat(lectureRoomQueryRepository.findByRoomNumberWithBuilding(lectureRoom.getBuilding(), lectureRoom.getRoomNumber()).isPresent()).isEqualTo(true);
  }

  @Test
  void 강의실_추가_중복_실패(){
    // Given
    LectureRoom lectureRoom = LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build();

    lectureRoomJpaRepository.save(lectureRoom);

    // When
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("D")
        .roomNumber(330)
        .capacity(50)
        .build();

    // Then
    Assertions.assertThatThrownBy(()-> lectureRoomCreateUseCase.create(req)).isInstanceOf(BusinessException.class).hasMessage(LectureRoomErrorCode.DUPLICATED_LECTURE_ROOM.getMessage());
  }

  @Test
  void 강의실_추가_건물만_같음_성공(){
    // Given
    LectureRoom lectureRoom = LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build();

    lectureRoomJpaRepository.save(lectureRoom);

    // When
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("D")
        .roomNumber(332)
        .capacity(30)
        .build();

    Long savedLectureRoomPk = lectureRoomCreateUseCase.create(req);

    // Then
    Assertions.assertThat(lectureRoomJpaRepository.findById(savedLectureRoomPk).isPresent()).isEqualTo(true);
  }

  @Test
  void 강의실_추가_호수만_같음_성공(){
    // Given
    LectureRoom lectureRoom = LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build();

    lectureRoomJpaRepository.save(lectureRoom);

    // When
    LectureRoomCreateDto.Request req = LectureRoomCreateDto.Request.builder()
        .building("G")
        .roomNumber(330)
        .capacity(30)
        .build();

    Long savedLectureRoomPk = lectureRoomCreateUseCase.create(req);

    // Then
    Assertions.assertThat(lectureRoomJpaRepository.findById(savedLectureRoomPk).isPresent()).isEqualTo(true);
  }


}
