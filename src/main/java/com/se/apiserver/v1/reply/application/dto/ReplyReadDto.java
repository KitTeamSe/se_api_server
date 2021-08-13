package com.se.apiserver.v1.reply.application.dto;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyReadDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel(value = "댓글 읽기 요청")
    public static class Response implements Comparable<Response>{
        private Long replyId;
        private Long postId;
        private String text;
        private String accountId;
        private String anonymousNickname;
        private List<AttachDto> attacheList;
        private List<Response> child;

        public static Response fromEntity(Reply reply, Boolean hasManageAuthority, Boolean hasAccessAuthority){
            ResponseBuilder responseBuilder = new ResponseBuilder();
            responseBuilder
                    .replyId(reply.getReplyId())
                    .postId(reply.getPost().getPostId());
            if(reply.getIsDelete() == ReplyIsDelete.DELETED && !hasManageAuthority){
                responseBuilder.text(Reply.DELETED_REPLY_TEXT);
            }else{
                responseBuilder.text(reply.getText());
            }

            if(reply.getIsSecret() == ReplyIsSecret.SECRET && reply.getIsDelete() != ReplyIsDelete.DELETED) {
                if (hasManageAuthority || hasAccessAuthority) {
                    responseBuilder.text(reply.getText());
                } else {
                    responseBuilder.text(Reply.SECRET_REPLY_TEXT);
                }
            }

            if(reply.getAccount() != null){
                responseBuilder.accountId(reply.getAccount().getIdString());
            }else{
                responseBuilder.anonymousNickname(reply.getAnonymous().getAnonymousNickname());
            }

            List<AttachDto> attaches = reply.getAttaches().stream()
                    .map(attach -> {
                        return new AttachDto(attach.getAttachId());
                    })
                    .collect(Collectors.toList());
            responseBuilder.attacheList(attaches);

            return responseBuilder.build();
        }

        public void addChild(Response response) {
            if (child == null) {
                child = new ArrayList<>();
            }
            child.add(response);
        }

        @Override
        public int compareTo(Response response) {
            return this.replyId.compareTo(response.getReplyId());
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AttachDto{
            private Long attachId;
        }

    }

    @Builder
    @Getter
    public static class ResponseListWithPage {
        List<Response> responseList;
        int totalData;
        int totalPage;
        int currentPage;
        int perPage;
    }
}
