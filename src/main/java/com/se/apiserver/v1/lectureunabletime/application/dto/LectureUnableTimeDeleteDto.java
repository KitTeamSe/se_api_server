package com.se.apiserver.v1.lectureunabletime.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureUnableTimeDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("강의 불가 시간 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 강의 불가 시간 id", example = "1")
    private Long lectureUnableTimeId;
  }
}
