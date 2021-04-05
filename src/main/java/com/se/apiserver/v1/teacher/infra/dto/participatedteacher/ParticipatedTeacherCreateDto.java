package com.se.apiserver.v1.teacher.infra.dto.participatedteacher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
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

    @ApiModelProperty(notes = "교원 번호", example = "1")
    private Long teacherId;

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("참여 교원 생성 응답")
  static public class Resposne{
    @ApiModelProperty(notes = "참여 교원 pk", example = "1")
    private Long participatedTeacherId;
  }
}
