package com.se.apiserver.v1.post.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.post.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsDeleted;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostReadDto {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
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


    public static Response fromEntity(Post post, boolean isOwnerOrManager) {
      ResponseBuilder builder = Response.builder()
          .postId(post.getPostId())
          .boardId(post.getBoard().getBoardId())
          .views(post.getViews())
          .isSecret(post.getIsSecret())
          .isNotice(post.getIsNotice());

      buildWriterInfo(builder, post);

      if (post.getIsSecret() == PostIsSecret.SECRET && !isOwnerOrManager) {
        return builder.build();
      }

      return builder
          .title(post.getTitle())
          .text(post.getText())
          .build();
    }

    private static void buildWriterInfo(ResponseBuilder builder, Post post) {
      if (post.getAccount() != null) {
        builder.accountId(post.getAccount().getAccountId());
        builder.accountNickname(post.getAccount().getNickname());
        return;
      }

      builder.anonymousNickname(post.getAnonymous().getAnonymousNickname());
    }
  }

}
