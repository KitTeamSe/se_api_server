package com.se.apiserver.v1.common.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;

import lombok.*;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel("페이지 요청")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

  @ApiModelProperty(example = "0", notes = "페이지, 0 이상만 가능")
  @Min(value = 0)
  private int page;
  @ApiModelProperty(example = "50", notes = "페이지의 사이즈, 10 이상 50 이하만 가능")
  @Min(value = 10)
  private int size;

  @ApiModelProperty(example = "ASC", notes = "정렬 방향, 기준(생성일)")
  private Sort.Direction direction;

  public void setPage(int page) {
    this.page = Math.max(page, 0);
  }

  public void setSize(int size) {
    int DEFAULT_SIZE = 50;
    int MAX_SIZE = 50;
    this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
  }

  public void setDirection(Sort.Direction direction) {
    this.direction = direction;
  }

  public org.springframework.data.domain.PageRequest of() {
    return org.springframework.data.domain.PageRequest.of(page, size, direction, "createdAt");
  }
}
