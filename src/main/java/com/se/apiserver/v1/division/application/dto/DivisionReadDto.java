package com.se.apiserver.v1.division.application.dto;

import com.se.apiserver.v1.division.domain.entity.Division;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DivisionReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("분반 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "분반 ID", example = "1")
    private Long divisionId;

    @ApiModelProperty(notes = "개설 교과 ID", example = "1")
    private Long openSubjectId;

    @ApiModelProperty(notes = "분반 번호", example = "1")
    private Integer divisionNumber;

    @ApiModelProperty(notes = "배치된 주간 수업 시간", example = "1")
    private Integer deployedTeachingTime;


    public static Response fromEntity(Division division){
      return Response.builder()
          .divisionId(division.getDivisionId())
          .openSubjectId(division.getOpenSubject().getOpenSubjectId())
          .divisionNumber(division.getDivisionNumber())
          .deployedTeachingTime(division.getDeployedTeachingTime())
          .build();
    }
  }
}
