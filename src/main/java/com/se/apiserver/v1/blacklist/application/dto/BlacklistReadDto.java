package com.se.apiserver.v1.blacklist.application.dto;

import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BlacklistReadDto {
    @NoArgsConstructor
    @Builder
    @Getter
    @ApiModel("블랙리스트 조회 응답")
    static public class Response{
        @ApiModelProperty(notes = "블랙리스트 아이디", example = "1")
        Long id;

        @ApiModelProperty(notes = "아이피", example = "127.0.0.1")
        String ip;

        @ApiModelProperty(notes = "사유", example = "광고성 댓글")
        String reason;

        public Response(Long id, String ip, String reason) {
            this.id = id;
            this.ip = ip;
            this.reason = reason;
        }

        public static Response fromEntity(Blacklist blacklist) {
            return Response.builder()
                    .id(blacklist.getBlacklistId())
                    .ip(blacklist.getIp())
                    .reason(blacklist.getReason())
                    .build();
        }
    }
}
