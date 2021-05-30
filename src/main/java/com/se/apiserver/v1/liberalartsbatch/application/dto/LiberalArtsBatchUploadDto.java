package com.se.apiserver.v1.liberalartsbatch.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LiberalArtsBatchUploadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("교양 배치 파일 업로드 응답")
  static public class Response{

    @ApiModelProperty(notes = "신규 배치 갯수", example = "1")
    private Integer newlyDeployed;

    @ApiModelProperty(notes = "배치 경고 로그")
    private String deploymentAlertLog;

    @ApiModelProperty(notes = "오류 로그")
    private String errorLog;
  }
}
