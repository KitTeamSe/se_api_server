package com.se.apiserver.v1.post.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.account.domain.entity.AccountType;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.*;

import com.se.apiserver.v1.tag.domain.entity.Tag;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

public class PostReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class SearchRequest{

    @ApiModelProperty(notes = "게시판 아이디", example = "1")
    private Long boardId;

    @ApiModelProperty(notes = "검색 키워드", example = "검색할 문자열")
    @Size(min = 1)
    private String keyword;

    @ApiModelProperty(notes = "검색 유형(TITLE_TEXT, TITLE, TEXT, REPLY...", example = "TITLE_TEXT")
    private PostSearchType postSearchType;

    @NotNull
    private PageRequest pageRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class PostListResponse{
    private Long boardId;
    private String boardNameEng;
    private String boardNameKor;
    private PageImpl<PostListItem> postListItem;

    public static PostListResponse fromEntity(PageImpl<PostListItem> postListItem, Board board){
      return PostListResponse.builder()
              .postListItem(postListItem)
              .boardId(board.getBoardId())
              .boardNameEng(board.getNameEng())
              .boardNameKor(board.getNameKor())
              .build();
    }
  }


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class PostListItem{
    private Long postId;

    private Long boardId;

    private Integer views;

    private Integer numReply;

    private PostIsSecret isSecret;

    private PostIsNotice isNotice;

    private String title;

    private String previewText;

    private String nickname;

    private AccountType accountType;

    private LocalDateTime createAt;

    @JsonInclude(Include.NON_NULL)
    private List<TagDto> tags;

    public static PostListItem fromEntity(Post post){
      String nickname = post.getAccount() != null ? post.getAccount().getNickname() : post.getAnonymous().getAnonymousNickname();
      String previewText = "";
      if(post.getIsSecret() == PostIsSecret.NORMAL){
        previewText = post.getPostContent().getText().length() <= 30 ? post.getPostContent().getText() : post.getPostContent().getText().substring(0, 30);
      }

      PostListItemBuilder builder = PostListItem.builder();

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.fromEntity(t))
          .collect(Collectors.toList())
      );

      AccountType accountType = post.getAccount() == null ? AccountType.ANONYMOUS : post.getAccount().getType();

      return builder
              .postId(post.getPostId())
              .boardId(post.getBoard().getBoardId())
              .views(post.getViews())
              .numReply(post.getNumReply())
              .isNotice(post.getIsNotice())
              .isSecret(post.getIsSecret())
              .title(post.getPostContent().getTitle())
              .previewText(previewText)
              .nickname(nickname)
              .accountType(accountType)
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

    private String boardNameEng;

    private String boardNameKor;

    private Integer views;

    private PostIsSecret isSecret;

    private PostIsNotice isNotice;

    private String nickname;

    private AccountType accountType;

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
      Board board = post.getBoard();
      ResponseBuilder builder = Response.builder()
          .postId(post.getPostId())
          .boardId(post.getBoard().getBoardId())
          .boardNameEng(board.getNameEng())
          .boardNameKor(board.getNameKor())
          .views(post.getViews())
          .isSecret(post.getIsSecret())
          .isNotice(post.getIsNotice())
          .createdAt(post.getCreatedAt());

      String nickname = post.getAccount() != null ? post.getAccount().getNickname() : post.getAnonymous().getAnonymousNickname();
      builder.nickname(nickname);

      AccountType accountType = post.getAccount() == null ? AccountType.ANONYMOUS : post.getAccount().getType();
      builder.accountType(accountType);

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.fromEntity(t))
          .collect(Collectors.toList())
      );

      if (post.getIsSecret() == PostIsSecret.SECRET && !isOwnerOrManager)
        throw new BusinessException(PostErrorCode.CAN_NOT_ACCESS_POST);

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

    static public TagDto fromEntity(Tag tag){
      return TagDto.builder()
          .tagId(tag.getTagId())
          .tag(tag.getText())
          .build();
    }
  }

}
