package com.se.apiserver.v1.lectureroom.infra.dto;

import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("강의실 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "강의실 id", example = "1")
    private Long lectureRoomId;

    @ApiModelProperty(notes = "건물명", example = "DB")
    private String building;

    @ApiModelProperty(notes = "호수(방 번호)", example = "107")
    private Integer roomNumber;

    @ApiModelProperty(notes = "정원", example = "30")
    private Integer capacity;

    public static Response fromEntity(LectureRoom lectureRoom){
      return Response.builder()
          .lectureRoomId(lectureRoom.getLectureRoomId())
          .building(lectureRoom.getBuilding())
          .roomNumber(lectureRoom.getRoomNumber())
          .capacity(lectureRoom.getCapacity())
          .build();
    }
  }

}
