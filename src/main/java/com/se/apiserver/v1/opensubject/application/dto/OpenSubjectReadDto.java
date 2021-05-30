package com.se.apiserver.v1.opensubject.application.dto;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto.Response;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;
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

    @ApiModelProperty(notes = "시간표 번호", example = "1")
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

    @ApiModelProperty(notes = "개설 교과 번호", example = "1")
    private Long openSubjectId;

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "교과 번호", example = "1")
    private Long subjectId;

    @ApiModelProperty(notes = "교과 이름", example = "자료구조")
    private String subjectName;

    @ApiModelProperty(notes = "분반 목록")
    private List<DivisionReadDto.Response> divisions;

    @ApiModelProperty(notes = "주간 수업 시간", example = "3")
    private Integer teachingTimePerWeek;

    @ApiModelProperty(notes = "자동 생성 여부", example = "false")
    private Boolean autoCreated;

    @ApiModelProperty(notes = "비고", example = "비고 예시")
    private String note;

    public static Response fromEntity(OpenSubject openSubject){
      Response.ResponseBuilder builder = Response.builder();
      builder.divisions(openSubject.getDivisions().stream()
          .map(DivisionReadDto.Response::fromEntity)
          .collect(Collectors.toList()));

      return builder
          .openSubjectId(openSubject.getOpenSubjectId())
          .timeTableId(openSubject.getTimeTable().getTimeTableId())
          .subjectId(openSubject.getSubject().getSubjectId())
          .subjectName(openSubject.getSubject().getName())
          .teachingTimePerWeek(openSubject.getTeachingTimePerWeek())
          .autoCreated(openSubject.getAutoCreated())
          .note(openSubject.getNote())
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("개설 교과 목록 조회 응답")
  static public class OpenSubjectListItem{
    @ApiModelProperty(notes = "개설 교과 번호", example = "1")
    private Long openSubjectId;

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "교과 번호", example = "1")
    private Long subjectId;

    @ApiModelProperty(notes = "교과 이름", example = "자료구조")
    private String subjectName;

    @ApiModelProperty(notes = "분반 개수")
    private Integer numberOfDivisions;

    @ApiModelProperty(notes = "주간 수업 시간", example = "3")
    private Integer teachingTimePerWeek;

    @ApiModelProperty(notes = "자동 생성 여부", example = "false")
    private Boolean autoCreated;

    @ApiModelProperty(notes = "비고", example = "비고 예시")
    private String note;

    public static OpenSubjectListItem fromEntity(OpenSubject openSubject){
      OpenSubjectListItemBuilder builder = OpenSubjectListItem.builder();

      return builder
          .openSubjectId(openSubject.getOpenSubjectId())
          .timeTableId(openSubject.getTimeTable().getTimeTableId())
          .subjectId(openSubject.getSubject().getSubjectId())
          .subjectName(openSubject.getSubject().getName())
          .numberOfDivisions(openSubject.getDivisions().size())
          .teachingTimePerWeek(openSubject.getTeachingTimePerWeek())
          .autoCreated(openSubject.getAutoCreated())
          .note(openSubject.getNote())
          .build();
    }
  }
}
