package com.se.apiserver.v1.post.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.post.domain.entity.*;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private List<AttachDto.Response> attaches;

    @JsonInclude(Include.NON_NULL)
    private List<TagDto.Response> tags;

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
          .map(a -> AttachDto.Response.fromEntity(a))
          .collect(Collectors.toList())
      );

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.Response.fromEntity(t))
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

}
