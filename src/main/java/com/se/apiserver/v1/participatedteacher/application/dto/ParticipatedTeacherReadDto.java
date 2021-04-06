package com.se.apiserver.v1.participatedteacher.application.dto;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ParticipatedTeacherReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("참여 교원 조회 요청")
  static public class Request{

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "페이장 정보")
    private PageRequest pageRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("참여 교원 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "참여 교원 id", example = "1")
    private Long participatedTeacherId;

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "교원 id", example = "1")
    private Long teacherId;

    public static Response fromEntity(ParticipatedTeacher participatedTeacher){
      return Response.builder()
          .participatedTeacherId(participatedTeacher.getParticipatedTeacherId())
          .timeTableId(participatedTeacher.getTimeTable().getTimeTableId())
          .teacherId(participatedTeacher.getTeacher().getTeacherId())
          .build();
    }
  }
}
