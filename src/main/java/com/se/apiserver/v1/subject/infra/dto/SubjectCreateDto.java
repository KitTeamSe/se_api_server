package com.se.apiserver.v1.subject.infra.dto;

import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SubjectCreateDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("교과 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "교육과정", example = "컴퓨터소프트웨어공학")
    @Size(min = 2, max = 30)
    private String curriculum;

    @ApiModelProperty(notes = "교과 구분", example = "MAJOR")
    private SubjectType type;

    @ApiModelProperty(notes = "교과목코드", example = "CS00001")
    @Size(min = 2, max = 30)
    private String code;

    @ApiModelProperty(notes = "교과목명", example = "논리회로")
    @Size(min = 2, max = 30)
    private String name;

    @ApiModelProperty(notes = "대상학년", example = "1")
    private Integer grade;

    @ApiModelProperty(notes = "개설학기", example = "1")
    private Integer semester;

    @ApiModelProperty(notes = "학점", example = "3")
    private Integer credit;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("교과 생성 응답")
  static public class Response{
    @ApiModelProperty(notes = "교과 pk", example = "1")
    private Long subjectId;
  }

}
