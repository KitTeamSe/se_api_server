package com.se.apiserver.v1.common.application;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Getter
@Setter
public class PaginationRequest<T> extends BaseRequest<T> {
  @ApiModelProperty(example = "0", notes = "페이지, 0 이상만 가능")
  @Min(value = 0)
  private int page;
  @ApiModelProperty(example = "50", notes = "페이지의 사이즈, 10 이상 50 이하만 가능")
  @Min(value = 10)
  private int size;
  @ApiModelProperty(example = "ASC", notes = "정렬 방향")
  private Sort.Direction direction;
  @ApiModelProperty(example = "id", notes = "정렬 기준")
  private String orderBy;

  public PaginationRequest() {
  }

  public PaginationRequest(T dto, int page, int size,
      Direction direction, String orderBy) {
    super(dto);
    this.page = page;
    this.size = size;
    this.direction = direction;
    this.orderBy = orderBy;
  }

  public PageRequest of(){
    return PageRequest.of(this.page,this.size, this.direction, this.orderBy);
  }
}