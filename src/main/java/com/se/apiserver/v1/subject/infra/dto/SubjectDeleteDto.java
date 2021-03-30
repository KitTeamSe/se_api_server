package com.se.apiserver.v1.subject.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SubjectDeleteDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel("교과 삭제 요청")
  static public class Request{

    @ApiModelProperty(notes = "삭제할 교과 id", example = "1")
    private Long subjectId;
  }
}
