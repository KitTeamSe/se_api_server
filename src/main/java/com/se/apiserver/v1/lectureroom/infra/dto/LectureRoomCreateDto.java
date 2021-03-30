package com.se.apiserver.v1.lectureroom.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel("강의실 생성 요청")
  @Builder
  static public class Request{
    @ApiModelProperty(notes = "건물명", example = "DB")
    @Size(min = 1, max = 30)
    private String building;

    @ApiModelProperty(notes = "호수(방 번호)", example = "107")
    private Integer roomNumber;

    @ApiModelProperty(notes = "정원", example = "50")
    private Integer capacity;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("강의실 생성 응답")
  static public class Response{
    @ApiModelProperty(notes = "강의실 pk", example = "1")
    private Long lectureRoomId;
  }
}
