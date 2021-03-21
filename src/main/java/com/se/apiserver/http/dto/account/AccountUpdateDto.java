package com.se.apiserver.http.dto.account;

import com.se.apiserver.domain.entity.account.AccountType;
import com.se.apiserver.domain.entity.account.InformationOpenAgree;
import com.se.apiserver.domain.entity.account.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AccountUpdateDto {
    @Data
    @ApiModel("회원 정보 수정 요청")
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
