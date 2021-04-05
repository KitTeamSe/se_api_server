package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomReadServiceTest {

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomReadService lectureRoomReadService;

  @Test
  void 강의실_조회_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    // When
    LectureRoomReadDto.Response response = lectureRoomReadService.read(lectureRoom.getLectureRoomId());

    // Then
    Assertions.assertThat(lectureRoom.getBuilding()).isEqualTo(response.getBuilding());
    Assertions.assertThat(lectureRoom.getRoomNumber()).isEqualTo(response.getRoomNumber());
    Assertions.assertThat(lectureRoom.getCapacity()).isEqualTo(response.getCapacity());
  }

  @Test
  void 강의실_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      lectureRoomReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM.getMessage());
  }

  @Test
  void 강의실_전체_조회_성공(){
    // Given
    lectureRoomJpaRepository.save(lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build()));

    lectureRoomJpaRepository.save(lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("DB")
        .roomNumber(107)
        .capacity(40)
        .build()));

    // When
    PageImpl responses = lectureRoomReadService.readAll(PageRequest.builder()
        .size(100)
        .direction(Sort.Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
