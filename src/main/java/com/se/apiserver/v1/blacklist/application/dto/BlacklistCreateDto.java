package com.se.apiserver.v1.blacklist.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class BlacklistCreateDto {

    @ApiModel("블랙리스트 등록 요청")
    @Getter
    static public class Request{
        @ApiModelProperty(notes = "아이피", example = "127.0.0.1")
        @Size(min = 4, max = 20)
        String ip;

        @ApiModelProperty(notes = "고유아이디", example = "1" )
        private Long accountId;

        @ApiModelProperty(notes = "사유", example = "광고성 댓글")
        @Size(min = 4, max = 20)
        String reason;

        @ApiModelProperty(notes = "차단 종료 날짜")
        private LocalDateTime releaseDate;
    }

}
