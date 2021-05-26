package com.se.apiserver.v1.division.application.dto;

import com.se.apiserver.v1.division.application.dto.DivisionReadDto.Response;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DivisionCheckDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("분반 배치 확인 응답")
  static public class Response{

    @ApiModelProperty(notes = "분반 ID", example = "1")
    private Long divisionId;

    @ApiModelProperty(notes = "분반 번호", example = "1")
    private Integer divisionNumber;

    @ApiModelProperty(notes = "완료 여부", example = "true")
    private Boolean isCompleted;

    @ApiModelProperty(notes = "배치된 수업 시간", example = "0")
    private Integer deployedTeachingTime;

    @ApiModelProperty(notes = "주간 수업 시간", example = "0")
    private Integer teachingTimePerWeek;

    @ApiModelProperty(notes = "미완료 사유", example = "null")
    private DivisionErrorCode divisionErrorCode;
  }
}
