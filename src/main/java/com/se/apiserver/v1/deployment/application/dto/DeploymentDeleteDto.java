package com.se.apiserver.v1.deployment.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeploymentDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("배치 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 배치 번호", example = "1")
    private Long deploymentId;
  }
}
