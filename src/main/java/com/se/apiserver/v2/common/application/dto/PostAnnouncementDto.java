package com.se.apiserver.v2.common.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.post.application.dto.PostReadDto.TagDto;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostAnnouncementDto {

  private Long postId;

  private Long boardId;

  private Integer views;

  private Integer numReply;

  private PostIsSecret isSecret;

  private String title;

  private String previewText;

  private String nickname;

  private AccountType accountType;

  private LocalDateTime createAt;

  @JsonInclude(Include.NON_NULL)
  private List<TagDto> tags;

  public static PostAnnouncementDto fromEntity(Post post) {
    System.out.println(post.getAccount().getNickname());
    String nickname = post.getAccount() != null ? post.getAccount().getNickname()
        : post.getAnonymous().getAnonymousNickname();
    String previewText = "";
    if (post.getIsSecret() == PostIsSecret.NORMAL) {
      previewText = post.getPostContent().getText().length() <= 30 ? post.getPostContent().getText()
          : post.getPostContent().getText().substring(0, 30);
    }

    PostAnnouncementDtoBuilder builder = PostAnnouncementDto.builder();

    builder.tags(post.getTags().stream()
        .map(TagDto::fromEntity)
        .collect(Collectors.toList())
    );

    AccountType accountType =
        post.getAccount() == null ? AccountType.ANONYMOUS : post.getAccount().getType();

    return builder
        .postId(post.getPostId())
        .boardId(post.getBoard().getBoardId())
        .views(post.getViews())
        .numReply(post.getNumReply())
        .isSecret(post.getIsSecret())
        .title(post.getPostContent().getTitle())
        .previewText(previewText)
        .nickname(nickname)
        .accountType(accountType)
        .createAt(post.getCreatedAt())
        .build();
  }
}
