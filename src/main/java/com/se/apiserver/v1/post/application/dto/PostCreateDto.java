package com.se.apiserver.v1.post.application.dto;

import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.post.domain.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;
import lombok.*;

import java.util.List;

public class PostCreateDto {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel(value = "게시글 생성 요청")
  static public class Request {

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(notes = "게시판 영문명", example = "freeboard")
    private String boardNameEng;

    @Valid
    @NotNull
    private PostContent postContent;

    @Valid
    @ApiModelProperty(notes = "익명 사용자 정보, 회원으로 등록일 경우 생략")
    private Anonymous anonymous;

    @NotNull
    @ApiModelProperty(notes = "공지로 설정할 것인지(관리자용)", example = "NORMAL")
    private PostIsNotice isNotice;

    @NotNull
    @ApiModelProperty(notes = "비밀글로 설정할 것인지(관리자용)", example = "NORMAL")
    private PostIsSecret isSecret;

    @ApiModelProperty(notes = "태그들")
    @Singular("tagList")
    private List<TagDto> tagList;

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

  @ApiModel("게시글 태그 등록")
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class TagDto {

    @ApiModelProperty(notes = "태그 아이디", example = "1")
    private Long tagId;
  }
}
