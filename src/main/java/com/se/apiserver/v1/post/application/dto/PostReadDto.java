package com.se.apiserver.v1.post.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class ListResponse{
    private Long postId;

    private Long boardId;

    private Integer views;

    private Integer numReply;

    private PostIsSecret isSecret;

    private PostIsNotice isNotice;

    private String title;

    private String previewText;

    private String nickname;

    private LocalDateTime createAt;

    public static ListResponse fromEntity(Post post){
      String nickname = post.getAccount() != null ? post.getAccount().getNickname() : post.getAnonymous().getAnonymousNickname();
      String previewText = "";
      if(post.getIsSecret() == PostIsSecret.NORMAL){
        previewText = post.getPostContent().getText().length() <= 30 ? post.getPostContent().getText() : post.getPostContent().getText().substring(0, 30);
      }

      return ListResponse.builder()
              .postId(post.getPostId())
              .boardId(post.getBoard().getBoardId())
              .views(post.getViews())
              .numReply(post.getNumReply())
              .isNotice(post.getIsNotice())
              .isSecret(post.getIsSecret())
              .title(post.getPostContent().getTitle())
              .previewText(previewText)
              .nickname(nickname)
              .createAt(post.getCreatedAt())
              .build();
    }
  }


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
    private String accountNickname;

    @JsonInclude(Include.NON_NULL)
    private String anonymousNickname;

    @JsonInclude(Include.NON_NULL)
    private PostContent postContent;

    private LocalDateTime createdAt;

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
          .isNotice(post.getIsNotice())
          .createdAt(post.getCreatedAt());

      if (post.getAccount() != null) {
        builder.accountNickname(post.getAccount().getNickname());
      }

      if(post.getAnonymous() != null)
        builder.anonymousNickname(post.getAnonymous().getAnonymousNickname());

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.fromEntity(t))
          .collect(Collectors.toList())
      );

      if (post.getIsSecret() == PostIsSecret.SECRET && !isOwnerOrManager) {
        return builder.build();
      }

      builder.attaches(post.getAttaches().stream()
          .map(a -> AttachDto.fromEntity(a))
          .collect(Collectors.toList())
      );

      return builder
          .postContent(post.getPostContent())
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
