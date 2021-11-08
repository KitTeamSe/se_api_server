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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

public class PostReadDto {

  @Getter
  @NoArgsConstructor
  @Builder
  @ApiModel(value = "게시글 검색 요청")
  static public class PostSearchRequest {

    @ApiModelProperty(notes = "게시판 영문명", example = "freeboard")
    private String boardNameEng;

    @ApiModelProperty(notes = "검색 키워드", example = "검색할 문자열")
    @Size(min = 1)
    private String keyword;

    @ApiModelProperty(notes = "검색 유형(TITLE_TEXT, TITLE, TEXT, REPLY...", example = "TITLE_TEXT")
    private PostSearchType postSearchType;

    @NotNull
    private PageRequest pageRequest;

    public PostSearchRequest(String boardNameEng, String keyword,
        PostSearchType postSearchType, PageRequest pageRequest) {
      this.boardNameEng = boardNameEng;
      this.keyword = keyword;
      this.postSearchType = postSearchType;
      this.pageRequest = pageRequest;
    }
  }

  @Getter
  @NoArgsConstructor
  @Builder
  @ApiModel(value = "게시글 읽기 요청")
  static public class PostListResponse {

    private Long boardId;
    private String boardNameEng;
    private String boardNameKor;
    private PageImpl<PostListItem> postListItem;

    public PostListResponse(Long boardId, String boardNameEng, String boardNameKor,
        PageImpl<PostListItem> postListItem) {
      this.boardId = boardId;
      this.boardNameEng = boardNameEng;
      this.boardNameKor = boardNameKor;
      this.postListItem = postListItem;
    }

    public static PostListResponse fromEntity(PageImpl<PostListItem> postListItem, Board board) {
      return PostListResponse.builder()
          .postListItem(postListItem)
          .boardId(board.getBoardId())
          .boardNameEng(board.getNameEng())
          .boardNameKor(board.getNameKor())
          .build();
    }
  }


  @Getter
  @NoArgsConstructor
  @Builder
  static public class PostListItem {

    private Long postId;

    private Long boardId;

    private Integer views;

    private Integer numReply;

    private PostIsSecret isSecret;

    private PostIsNotice isNotice;

    private String title;

    private String previewText;

    private String accountIdString;

    private String nickname;

    private AccountType accountType;

    private String ip;

    private LocalDateTime createAt;

    @JsonInclude(Include.NON_NULL)
    private List<TagDto> tags;

    public PostListItem(Long postId, Long boardId, Integer views, Integer numReply,
        PostIsSecret isSecret, PostIsNotice isNotice, String title, String previewText,
        String accountIdString, String nickname,
        AccountType accountType, String ip, LocalDateTime createAt,
        List<TagDto> tags) {
      this.postId = postId;
      this.boardId = boardId;
      this.views = views;
      this.numReply = numReply;
      this.isSecret = isSecret;
      this.isNotice = isNotice;
      this.title = title;
      this.previewText = previewText;
      this.accountIdString = accountIdString;
      this.nickname = nickname;
      this.accountType = accountType;
      this.ip = ip;
      this.createAt = createAt;
      this.tags = tags;
    }

    public static PostListItem fromEntity(Post post) {
      String previewText = "";
      if (post.getIsSecret() == PostIsSecret.NORMAL) {
        previewText =
            post.getPostContent().getText().length() <= 30 ? post.getPostContent().getText()
                : post.getPostContent().getText().substring(0, 30);
      }

      PostListItemBuilder builder = PostListItem.builder();

      if (post.getAccount() != null) {
        String nickname = post.getAccount().getNickname();
        String accountIdString = post.getAccount().getIdString();
        builder.nickname(nickname);
        builder.accountIdString(accountIdString);

      } else {
        String nickname = post.getAnonymous().getAnonymousNickname();
        builder.nickname(nickname);
        String ip = post.getLastModifiedIp() == null ? post.getCreatedIp() : post.getLastModifiedIp();
        builder.ip(hideIpHalf(ip));
      }

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.fromEntity(t))
          .collect(Collectors.toList())
      );

      AccountType accountType =
          post.getAccount() == null ? AccountType.ANONYMOUS : post.getAccount().getType();

      return builder
          .postId(post.getPostId())
          .boardId(post.getBoard().getBoardId())
          .views(post.getViews())
          .numReply(post.getReplies().size())
          .isNotice(post.getIsNotice())
          .isSecret(post.getIsSecret())
          .title(post.getPostContent().getTitle())
          .previewText(previewText)
          .accountType(accountType)
          .createAt(post.getCreatedAt())
          .build();
    }
  }


  @Getter
  @NoArgsConstructor
  @Builder
  static public class Response {

    private Long postId;

    private Long boardId;

    private String boardNameEng;

    private String boardNameKor;

    private Integer views;

    private PostIsSecret isSecret;

    private PostIsNotice isNotice;

    private String accountIdString;

    private String nickname;

    private AccountType accountType;

    private String ip;

    @JsonInclude(Include.NON_NULL)
    private PostContent postContent;

    private LocalDateTime createdAt;

    @JsonInclude(Include.NON_NULL)
    private List<AttachDto> attaches;

    @JsonInclude(Include.NON_NULL)
    private List<TagDto> tags;

    public Response(Long postId, Long boardId, String boardNameEng, String boardNameKor,
        Integer views, PostIsSecret isSecret,
        PostIsNotice isNotice, String accountIdString, String nickname,
        AccountType accountType, String ip, PostContent postContent, LocalDateTime createdAt,
        List<AttachDto> attaches,
        List<TagDto> tags) {
      this.postId = postId;
      this.boardId = boardId;
      this.boardNameEng = boardNameEng;
      this.boardNameKor = boardNameKor;
      this.views = views;
      this.isSecret = isSecret;
      this.isNotice = isNotice;
      this.accountIdString = accountIdString;
      this.nickname = nickname;
      this.accountType = accountType;
      this.ip = ip;
      this.postContent = postContent;
      this.createdAt = createdAt;
      this.attaches = attaches;
      this.tags = tags;
    }

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

      if (post.getAccount() != null) {
        String nickname = post.getAccount().getNickname();
        String accountIdString = post.getAccount().getIdString();
        builder.nickname(nickname);
        builder.accountIdString(accountIdString);

      } else {
        String nickname = post.getAnonymous().getAnonymousNickname();
        builder.nickname(nickname);
        String ip = post.getLastModifiedIp() == null ? post.getCreatedIp() : post.getLastModifiedIp();
        builder.ip(hideIpHalf(ip));
      }

      AccountType accountType =
          post.getAccount() == null ? AccountType.ANONYMOUS : post.getAccount().getType();
      builder.accountType(accountType);

      builder.tags(post.getTags().stream()
          .map(t -> TagDto.fromEntity(t))
          .collect(Collectors.toList())
      );

      builder.attaches(post.getAttaches()
          .stream()
          .map(a -> AttachDto.fromEntity(a))
          .collect(Collectors.toList()));

      if (post.getIsSecret() == PostIsSecret.SECRET && !isOwnerOrManager) {
        throw new BusinessException(PostErrorCode.CAN_NOT_ACCESS_POST);
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

  @Getter
  @NoArgsConstructor
  @Builder
  static public class AttachDto {

    private Long attachId;

    private String downloadUrl;

    private String fileName;

    private Long fileSize;

    public AttachDto(Long attachId, String downloadUrl, String fileName, Long fileSize) {
      this.attachId = attachId;
      this.downloadUrl = downloadUrl;
      this.fileName = fileName;
      this.fileSize = fileSize;
    }

    static public AttachDto fromEntity(Attach attach) {
      return AttachDto.builder()
          .attachId(attach.getAttachId())
          .downloadUrl(attach.getDownloadUrl())
          .fileName(attach.getFileName())
          .fileSize(attach.getFileSize())
          .build();
    }
  }

  @Getter
  @NoArgsConstructor
  @Builder
  static public class TagDto {

    private Long tagId;

    private String tag;

    public TagDto(Long tagId, String tag) {
      this.tagId = tagId;
      this.tag = tag;
    }

    static public TagDto fromEntity(Tag tag) {
      return TagDto.builder()
          .tagId(tag.getTagId())
          .tag(tag.getText())
          .build();
    }
  }

  static private String hideIpHalf(String ip){
    StringBuilder sb = new StringBuilder();
    StringTokenizer st = new StringTokenizer(ip, ".");

    int delimiterCnt = 0;
    while(st.hasMoreTokens() && delimiterCnt < 2){
      sb.append(st.nextElement()).append(".");
      delimiterCnt++;
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }
}
