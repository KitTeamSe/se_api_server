package com.se.apiserver.v1.blacklist.application.dto;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BlacklistReadDto {
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    @ApiModel("블랙리스트 조회 응답")
    static public class Response{
        @ApiModelProperty(notes = "블랙리스트 아이디", example = "1")
        private Long id;

        @ApiModelProperty(notes = "아이피", example = "127.0.0.1")
        private String ip;

        @ApiModelProperty(notes = "고유아이디", example = "1")
        private Long accountId;

        @ApiModelProperty(notes = "사용자 아이디(로그인아이디)", example = "user")
        private String idString;

        @ApiModelProperty(notes = "사유", example = "광고성 댓글")
        private String reason;

        @ApiModelProperty(notes = "릴리즈 일시")
        private LocalDateTime releaseDate;

        public Response(Long id, String ip, Long accountId, String idString, String reason,
            LocalDateTime releaseDate) {
            this.id = id;
            this.ip = ip;
            this.accountId = accountId;
            this.idString = idString;
            this.reason = reason;
            this.releaseDate = releaseDate;
        }

        public static Response fromEntity(Blacklist blacklist) {
            return Response.builder()
                .id(blacklist.getBlacklistId())
                .ip(blacklist.getIp())
                .accountId(blacklist.getAccount().getAccountId())
                .idString(blacklist.getAccount().getIdString())
                .reason(blacklist.getReason())
                .releaseDate(blacklist.getReleaseDate())
                .build();
        }
    }
}
