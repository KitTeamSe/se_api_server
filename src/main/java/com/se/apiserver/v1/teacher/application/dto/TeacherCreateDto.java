package com.se.apiserver.v1.teacher.application.dto;

import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeacherCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("교원 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "이름", example = "홍길동")
    @Size(min = 2, max = 100)
    private String name;

    @ApiModelProperty(notes = "교원 구분", example = "FULL_PROFESSOR")
    private TeacherType type;

    @ApiModelProperty(notes = "소속", example = "컴퓨터소프트웨어공학")
    @Size(min = 2, max = 30)
    private String department;

    @ApiModelProperty(notes = "비고", example = "비고 예시")
    @Size(max = 255)
    private String note;
  }
}
