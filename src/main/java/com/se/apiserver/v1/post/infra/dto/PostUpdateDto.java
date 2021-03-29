package com.se.apiserver.v1.post.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostUpdateDto {
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel("게시글 등록 요청")
  static public class Request{
    @NotNull
    @Min(1)
    @ApiModelProperty(notes = "게시글 아이디", example = "1")
    private Long postId;

    @NotNull
    @Min(1)
    @ApiModelProperty(notes = "게시판 아이디", example = "1")
    private Long boardId;

    @Min(1)
    @ApiModelProperty(notes = "사용자 아이디(익명시 생략) ", example = "1")
    private Long accountId;

    @Size(min = 3, max = 50)
    @ApiModelProperty(notes = "제목", example = "게시글 제목")
    private String title;

    @Size(min = 5, max = 2000)
    @ApiModelProperty(notes = "내용", example = "게시글 내용")
    private String text;

    @Size(min = 2, max = 20)
    @ApiModelProperty(notes = "익명 사용자 아이디", example = "test1")
    private String anonymousId;

    @Size(min = 2, max = 20)
    @ApiModelProperty(notes = "익명 사용자 별명", example = "테스트유저")
    private String anonymousNickname;

    @Size(min = 2, max = 20)
    @ApiModelProperty(notes = "익명 사용자 비밀번호", example = "testest")
    private String anonymousPassword;

    @NotNull
    @ApiModelProperty(notes = "공지로 설정할 것인지(관리자용)", example = "NORMAL")
    private PostIsNotice isNotice;

    @NotNull
    @ApiModelProperty(notes = "비밀글로 설정할 것인지(관리자용)", example = "NORMAL")
    private PostIsSecret isSecret;
  }

}
