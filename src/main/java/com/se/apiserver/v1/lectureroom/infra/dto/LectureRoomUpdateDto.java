package com.se.apiserver.v1.lectureroom.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomUpdateDto {
  @Data
  @ApiModel("강의실 수정 요청")
  @Builder
  static public class Request{
    @ApiModelProperty(example = "DB", notes = "변경할 건물명")
    @Size(min = 1, max = 30)
    private String building;

    @ApiModelProperty(example = "107", notes = "변경할 호수(방 번호)")
    private Integer roomNumber;

    @ApiModelProperty(example = "50", notes = "변경할 정원")
    private Integer capacity;
  }
}
