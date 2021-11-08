package com.se.apiserver.v1.reply.application.dto;

import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringTokenizer;
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
  public static class Response {

    private Long replyId;
    private Long postId;
    private Long parentId;
    private String text;
    private String accountId;
    private String anonymousNickname;
    private String ip;
    private List<ReplyReadAttachDto> attacheList;
    private List<Response> child;
    private LocalDateTime createAt;
    private ReplyIsSecret isSecret;
    private ReplyIsDelete isDelete;

    public static Response fromEntity(Reply reply, Boolean hasManageAuthority,
        Boolean hasAccessAuthority) {
      ResponseBuilder responseBuilder = new ResponseBuilder();

      responseBuilder
          .replyId(reply.getReplyId())
          .postId(reply.getPost().getPostId())
          .isSecret(reply.getIsSecret())
          .isDelete(reply.getIsDelete());

      responseBuilder.text(getReplyText(reply, hasManageAuthority, hasAccessAuthority));

      if (reply.getAccount() != null) {
        responseBuilder.accountId(reply.getAccount().getIdString());
      } else {
        responseBuilder.anonymousNickname(reply.getAnonymous().getAnonymousNickname());
        String ip = reply.getLastModifiedIp() == null ? reply.getCreatedIp() : reply.getLastModifiedIp();
        responseBuilder.ip(hideIpHalf(ip));
      }

      if (reply.getParent() != null){
        responseBuilder.parentId(reply.getParent().getReplyId());
      }

      List<ReplyReadAttachDto> attaches = reply.getAttaches().stream()
          .map(attach -> new ReplyReadAttachDto(attach.getAttachId(), attach.getDownloadUrl(), attach.getFileName(), attach.getFileSize()))
          .collect(Collectors.toList());
      responseBuilder.attacheList(attaches);
      responseBuilder.createAt(reply.getCreatedAt());

      return responseBuilder.build();
    }

    public void addChild(Response response) {
      if (child == null) {
        child = new ArrayList<>();
      }
      child.add(response);
    }

    private static String getReplyText(Reply reply, Boolean hasManageAuthority, Boolean hasAccessAuthority){
      if (reply.getIsDelete() == ReplyIsDelete.DELETED && !hasManageAuthority) {
        return Reply.DELETED_REPLY_TEXT;
      }

      if (reply.getIsSecret() == ReplyIsSecret.SECRET && reply.getIsDelete() != ReplyIsDelete.DELETED) {
        if (!hasManageAuthority && !hasAccessAuthority) {
          return Reply.SECRET_REPLY_TEXT;
        }
      }
      return reply.getText();
    }

    @Getter
    @NoArgsConstructor
    public static class ReplyReadAttachDto {
      private Long attachId;
      private String downloadUrl;
      private String fileName;
      private Long fileSize;

      public ReplyReadAttachDto(Long attachId, String downloadUrl, String fileName, Long fileSize) {
        this.attachId = attachId;
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
      }
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

  static private String hideIpHalf(String ip){
    StringBuilder sb = new StringBuilder();
    StringTokenizer st = new StringTokenizer(ip, ".");

    int delimiterCnt = 0;
    while(st.hasMoreTokens() && delimiterCnt < 2){
      sb.append(st.nextElement()).append(".");
      delimiterCnt++;
    }
    if(sb.length() > 0)
      sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }
}
