package com.se.apiserver.v1.deployment.application.dto;

import com.se.apiserver.v1.deployment.domain.entity.DeploymentAlertMessage;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeploymentCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("배치 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "분반 번호", example = "1")
    private Long divisionId;

    @ApiModelProperty(notes = "사용 가능 강의실 번호", example = "1")
    private Long usableLectureRoomId;

    @ApiModelProperty(notes = "참여 교원 번호", example = "1")
    private Long participatedTeacherId;

    @ApiModelProperty(notes = "요일", example = "THURSDAY")
    private DayOfWeek dayOfWeek;

    @ApiModelProperty(notes = "시작 교시 번호", example = "1")
    private Long startPeriodId;

    @ApiModelProperty(notes = "종료 교시 번호", example = "1")
    private Long endPeriodId;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("배치 생성 응답")
  static public class Resposne{
    @ApiModelProperty(notes = "배치 pk", example = "1")
    private Long deploymentId;

    @ApiModelProperty(notes = "경고 메시지")
    private DeploymentAlertMessage deploymentAlertMessage;
  }
}
