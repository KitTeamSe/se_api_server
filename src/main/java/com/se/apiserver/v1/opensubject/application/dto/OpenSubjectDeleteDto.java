package com.se.apiserver.v1.opensubject.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OpenSubjectDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("개설 교과 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 개설 교과 번호", example = "1")
    private Long openSubjectId;
  }
}
