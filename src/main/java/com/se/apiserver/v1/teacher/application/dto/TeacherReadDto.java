package com.se.apiserver.v1.teacher.application.dto;

import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeacherReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("교원 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "교원 id", example = "1")
    private Long teacherId;

    @ApiModelProperty(notes = "이름", example = "홍길동")
    private String name;

    @ApiModelProperty(notes = "교원 구분", example = "FULL_PROFESSOR")
    private TeacherType type;

    @ApiModelProperty(notes = "소속", example = "컴퓨터소프트웨어공학")
    private String department;

    @ApiModelProperty(notes = "자동 생성 여부", example = "false")
    private Boolean autoCreated;

    @ApiModelProperty(notes = "비고", example = "비고 예시")
    private String note;

    public static Response fromEntity(Teacher teacher){
      return Response.builder()
          .teacherId(teacher.getTeacherId())
          .name(teacher.getName())
          .type(teacher.getType())
          .department(teacher.getDepartment())
          .autoCreated(teacher.getAutoCreated())
          .note(teacher.getNote())
          .build();
    }
  }
}
