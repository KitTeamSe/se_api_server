package com.se.apiserver.v1.teacher.infra.dto.teacher;

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
    @Size(min = 2, max = 20)
    private String name;

    @ApiModelProperty(notes = "교원 구분", example = "FULL_PROFESSOR")
    private TeacherType type;

    @ApiModelProperty(notes = "소속", example = "컴퓨터소프트웨어공학")
    @Size(min = 2, max = 30)
    private String department;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("교원 생성 응답")
  static public class Resposne{
    @ApiModelProperty(notes = "교원 pk", example = "1")
    private Long teacherId;
  }


}
