package com.se.apiserver.v1.opensubject.application.dto;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OpenSubjectReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("개설 교과 조회 요청")
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
  @ApiModel("개설 교과 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "개설 교과 id", example = "1")
    private Long openSubjectId;

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "교과 id", example = "1")
    private Long subjectId;

    public static Response fromEntity(OpenSubject openSubject){
      return Response.builder()
          .openSubjectId(openSubject.getOpenSubjectId())
          .timeTableId(openSubject.getTimeTable().getTimeTableId())
          .subjectId(openSubject.getSubject().getSubjectId())
          .build();
    }
  }
}
