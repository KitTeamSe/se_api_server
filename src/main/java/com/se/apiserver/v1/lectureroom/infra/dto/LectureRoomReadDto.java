package com.se.apiserver.v1.lectureroom.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomReadDto {
  @Data
  @Builder
  static public class Request{

    private String building;

    private Integer roomNumber;

    private Integer capacity;
  }

}
