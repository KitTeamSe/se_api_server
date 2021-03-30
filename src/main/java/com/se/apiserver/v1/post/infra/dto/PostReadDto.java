package com.se.apiserver.v1.post.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class Response {

    private Long postId;

    private Long boardId;

    private Integer views;

    private PostIsSecret isSecret;

    private PostIsNotice isNotice;

    @JsonInclude(Include.NON_NULL)
    private Long accountId;

    @JsonInclude(Include.NON_NULL)
    private String accountNickname;

    @JsonInclude(Include.NON_NULL)
    @Size(min = 2, max = 20)
    private String anonymousNickname;

    @JsonInclude(Include.NON_NULL)
    private String title;

    @JsonInclude(Include.NON_NULL)
    private String text;

//    @JsonInclude(Include.NON_NULL)
//    private List<Reply> replies = new ArrayList<>();

    @JsonInclude(Include.NON_NULL)
    private List<AttachDto> attaches;

    @JsonInclude(Include.NON_NULL)
    private List<TagDto> tags;

    public static Response fromEntity(Post post, boolean isOwnerOrManager) {
      ResponseBuilder builder = Response.builder()
          .postId(post.getPostId())
          .boardId(post.getBoard().getBoardId())
          .views(post.getViews())
          .isSecret(post.getIsSecret())
          .isNotice(post.getIsNotice());

      if (post.getAccount() != null) {
        builder.accountId(post.getAccount().getAccountId());
        builder.accountNickname(post.getAccount().getNickname());
      }

      if(post.getAnonymous() != null)
        builder.anonymousNickname(post.getAnonymous().getAnonymousNickname());

      builder.attaches(post.getAttaches().stream()
          .map(a -> AttachDto.fromEntity(a))
          .collect(Collectors.toList())
      );

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.fromEntity(t))
          .collect(Collectors.toList())
      );

      if (post.getIsSecret() == PostIsSecret.SECRET && !isOwnerOrManager) {
        return builder.build();
      }

      return builder
          .title(post.getTitle())
          .text(post.getText())
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class AttachDto{
    private Long attachId;

    private String downloadUrl;

    private String fileName;

    static public AttachDto fromEntity(Attach attach) {
      return AttachDto.builder()
          .attachId(attach.getAttachId())
          .downloadUrl(attach.getDownloadUrl())
          .fileName(attach.getFileName())
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class TagDto{
    private Long tagId;

    private String tag;

    static public TagDto fromEntity(PostTagMapping postTagMapping){
      return TagDto.builder()
          .tagId(postTagMapping.getTag().getTagId())
          .tag(postTagMapping.getTag().getText())
          .build();
    }
  }

}
