package com.se.apiserver.v1.usablelectureroom.application.dto;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsableLectureRoomReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("사용 가능 강의실 조회 요청")
  static public class Request{

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "페이장 정보")
    private PageRequest pageRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("사용 가능 강의실 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "사용 가능 강의실 id", example = "1")
    private Long usableLectureRoomId;

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "강의실 id", example = "1")
    private Long lectureRoomId;

    public static Response fromEntity(UsableLectureRoom usableLectureRoom){
      return Response.builder()
          .usableLectureRoomId(usableLectureRoom.getUsableLectureRoomId())
          .timeTableId(usableLectureRoom.getTimeTable().getTimeTableId())
          .lectureRoomId(usableLectureRoom.getLectureRoom().getLectureRoomId())
          .build();
    }
  }
}
