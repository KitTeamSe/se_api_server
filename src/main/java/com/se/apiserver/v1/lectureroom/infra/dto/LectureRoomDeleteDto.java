package com.se.apiserver.v1.lectureroom.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomDeleteDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("강의실 삭제 요청")
  static public class Request{
    @ApiModelProperty(example = "1", notes = "삭제할 강의실 pk")
    private Long lectureRoomId;
  }
}
