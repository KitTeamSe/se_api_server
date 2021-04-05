package com.se.apiserver.v1.account.application.dto;

import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.account.domain.entity.InformationOpenAgree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AccountUpdateDto {
    @Data
    @ApiModel("회원 정보 수정 요청")
    @Builder
    static public class Request{

        @Size(min = 4, max = 20)
        @NotEmpty
        @ApiModelProperty(notes = "사용자 아이디", example = "user")
        private String id;

        @Size(min = 8, max = 20)
        @ApiModelProperty(notes = "변경할 비밀번호", example = "ce75407540")
        private String password;

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "변경할 이름", example = "user")
        private String name;

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "변경할 닉네임", example = "account")
        private String nickname;

        @Size(min = 8, max = 20)
        @ApiModelProperty(notes = "변경할 학번", example = "account")
        private String studentId;

        @ApiModelProperty(notes = "변경할 계정 타입", example = "ASSISTANT")
        private AccountType type;

        @ApiModelProperty(notes = "변경할 정보 제공 동의 내역", example = "AGREE")
        private InformationOpenAgree informationOpenAgree;

        @ApiModelProperty(notes = "변경 질문 pk", example = "1")
        private Long questionId;

        @ApiModelProperty(notes = "변경 답변", example = "ans")
        @Size(min = 2, max = 100)
        private String answer;
    }
}
