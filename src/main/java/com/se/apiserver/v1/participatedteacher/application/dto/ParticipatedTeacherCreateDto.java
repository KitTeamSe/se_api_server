package com.se.apiserver.v1.participatedteacher.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ParticipatedTeacherCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("참여 교원 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "교원 번호", example = "1")
    private Long teacherId;
  }

}
