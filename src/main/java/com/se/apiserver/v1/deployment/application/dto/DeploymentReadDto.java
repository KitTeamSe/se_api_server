package com.se.apiserver.v1.deployment.application.dto;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeploymentReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("배치 조회 요청")
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
  @ApiModel("배치 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "배치 번호", example = "1")
    private Long deploymentId;

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "분반 번호", example = "1")
    private Long divisionId;

    @ApiModelProperty(notes = "사용 가능 강의실 번호", example = "1")
    private Long usableLectureRoomId;

    @ApiModelProperty(notes = "참여 교원 번호", example = "1")
    private Long participatedTeacher;

    @ApiModelProperty(notes = "요일", example = "MONDAY")
    private DayOfWeek dayOfWeek;

    @ApiModelProperty(notes = "시작 교시 번호", example = "1")
    private Long startPeriodId;

    @ApiModelProperty(notes = "종료 교시 번호", example = "1")
    private Long endPeriodId;

    public static Response fromEntity(Deployment deployment){
      return Response.builder()
          .deploymentId(deployment.getDeploymentId())
          .timeTableId(deployment.getTimeTable().getTimeTableId())
          .divisionId(deployment.getDivision().getDivisionId())
          .usableLectureRoomId(deployment.getUsableLectureRoom().getUsableLectureRoomId())
          .participatedTeacher(deployment.getParticipatedTeacher().getParticipatedTeacherId())
          .dayOfWeek(deployment.getDayOfWeek())
          .startPeriodId(deployment.getPeriodRange().getStartPeriod().getPeriodId())
          .endPeriodId(deployment.getPeriodRange().getEndPeriod().getPeriodId())
          .build();
    }
  }
}
