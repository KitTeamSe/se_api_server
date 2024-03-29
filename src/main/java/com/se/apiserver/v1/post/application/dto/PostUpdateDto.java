package com.se.apiserver.v1.post.application.dto;

import com.se.apiserver.v1.post.domain.entity.PostContent;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.List;
import lombok.Singular;

public class PostUpdateDto {
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel("게시글 수정 요청")
  static public class Request{
    @NotNull
    @Min(1)
    @ApiModelProperty(notes = "게시물 아이디", example = "1")
    private Long postId;

    @NotNull
    private PostContent postContent;

    @ApiModelProperty(notes = "익명 사용자 비밀번호, 회원으로 등록일 경우 생략")
    private String anonymousPassword;

    @NotNull
    @ApiModelProperty(notes = "공지로 설정할 것인지(관리자용)", example = "NORMAL")
    private PostIsNotice isNotice;

    @NotNull
    @ApiModelProperty(notes = "비밀글로 설정할 것인지(관리자용)", example = "NORMAL")
    private PostIsSecret isSecret;

    @ApiModelProperty(notes = "태그들")
    @Singular("tagList")
    private List<PostCreateDto.TagDto> tagList;

    @ApiModelProperty(notes = "첨부파일들")
    @Singular("attachmentList")
    private List<AttachDto> attachmentList;
  }

  @ApiModel("첨부 파일")
  @Getter
  @NoArgsConstructor
  @Builder
  static public class AttachDto {

    private Long attachId;

    public AttachDto(Long attachId) {
      this.attachId = attachId;
    }
  }

}
