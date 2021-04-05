package com.se.apiserver.v1.teacher.application.dto;

import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeacherUpdateDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("교원 수정 요청")
  static public class Request{

    @ApiModelProperty(notes = "교원 id", example = "1")
    private Long teacherId;

    @ApiModelProperty(notes = "변경할 이름", example = "고길동")
    private String name;

    @ApiModelProperty(notes = "변경할 교원 구분", example = "FIXED_TERM_PROFESSOR")
    private TeacherType type;

    @ApiModelProperty(notes = "변경할 소속", example = "기계공학과")
    private String department;
  }
}
