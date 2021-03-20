package com.se.apiserver.http.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Size;

@ApiModel("페이지 요청")
public class PageRequest {
    @ApiModelProperty(example = "1", notes = "페이지, 1 이상이여야 함")
    @Size(min = 1)
    private int page;
    @ApiModelProperty(example = "1", notes = "페이지의 사이즈, 1 이상이여야 함")
    @Size(min = 1)
    private int size;

    @ApiModelProperty(example = "ASC", notes = "정렬 방향")
    private Sort.Direction direction;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
    // getter

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createdAt");
    }
}
