package com.se.apiserver.v1.reply.application.dto;

import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

public class ReplyCreateDto {

  @Getter
  @NoArgsConstructor
  @Builder
  @ApiModel(value = "댓글 생성 요청")
  public static class Request {

    public Request(Long postId, String text, Anonymous anonymous, Long parentId, ReplyIsSecret isSecret, List<AttachDto> attachmentList) {
      this.postId = postId;
      this.text = text;
      this.anonymous = anonymous;
      this.parentId = parentId;
      this.isSecret = isSecret;
      this.attachmentList = attachmentList;
    }

    @NotNull
    @Min(1)
    @ApiModelProperty(notes = "게시글 아이디", example = "1")
    private Long postId;

    @NotNull
    @ApiModelProperty(notes = "댓글 내용", example = "string")
    private String text;

    @ApiModelProperty(notes = "익명 사용자 정보, 회원으로 등록일 경우 생략")
    private Anonymous anonymous;

    @Min(1)
    @ApiModelProperty(notes = "부모 댓글의 번호", example = "1")
    private Long parentId;

    @ApiModelProperty(notes = "비밀 여부")
    @NotNull
    private ReplyIsSecret isSecret;

    @ApiModelProperty(notes = "첨부파일들")
    @Singular("attachmentList")
    private List<AttachDto> attachmentList;
  }

  @Getter
  @NoArgsConstructor
  @Builder
  public static class AttachDto {
    private Long attachId;

    public AttachDto(Long attachId) {
      this.attachId = attachId;
    }
  }
}

