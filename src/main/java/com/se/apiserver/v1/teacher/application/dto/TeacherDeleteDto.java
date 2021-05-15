package com.se.apiserver.v1.teacher.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeacherDeleteDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("교원 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 교원 id", example = "1")
    private Long teacherId;
  }
}
