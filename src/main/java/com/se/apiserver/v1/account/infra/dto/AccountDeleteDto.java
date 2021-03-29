package com.se.apiserver.v1.account.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AccountDeleteDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("회원 삭제")
    static public class Request{
        @Size(min = 4, max = 20)
        @NotEmpty
        @ApiModelProperty(notes = "회원 아이디", example = "user")
        private String id;
    }
}
