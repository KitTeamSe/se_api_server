package com.se.apiserver.v1.common.application;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class BaseRequest<T> {
  @JsonUnwrapped
  @ApiModelProperty
  @Valid
  private T dto;

  public BaseRequest() {

  }

  public BaseRequest(T dto) {
    this.dto = dto;
  }
}
