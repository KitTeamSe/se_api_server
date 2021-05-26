package com.se.apiserver.v1.lectureroom.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomUpdateDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("강의실 수정 요청")
  static public class Request{
    @ApiModelProperty(notes = "강의실 id", example = "1")
    private Long lectureRoomId;

    @ApiModelProperty(notes = "변경할 건물명", example = "DB")
    @Size(min = 1, max = 30)
    private String building;

    @ApiModelProperty(notes = "변경할 호수(방 번호)", example = "107")
    private String roomNumber;

    @ApiModelProperty(notes = "변경할 정원", example = "50")
    private Integer capacity;

    @ApiModelProperty(notes = "변경할 비고", example = "정원이 50명인 DB 107")
    private String note;
  }
}
