package com.se.apiserver.v1.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MenuReadDto {

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @ApiModel("메뉴 목록 조회 응답")
  static public class ReadAllResponse {

    @JsonUnwrapped
    private ReadResponse readResponse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ReadAllResponse> child;

    public ReadAllResponse(
        ReadResponse readResponse,
        List<ReadAllResponse> child) {
      this.readResponse = readResponse;
      this.child = child;
    }

    @JsonValue
    public static MenuReadDto.ReadAllResponse fromEntity(Menu menu, Set<String> authorities) {
      MenuReadDto.ReadAllResponse.ReadAllResponseBuilder responseBuilder = ReadAllResponse.builder()
          .readResponse(ReadResponse.fromEntity(menu));

      List<ReadAllResponse> tmp = new ArrayList<>();
      for (Menu child : menu.getChild()) {
          if (child.canAccess(authorities)) {
              tmp.add(fromEntity(child, authorities));
          }
      }
      responseBuilder.child(tmp);
      return responseBuilder.build();
    }

  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @ApiModel("메뉴 조회 응답")
  static public class ReadResponse {

    private Long menuId;

    private String nameEng;

    private String nameKor;

    private Integer menuOrder;

    private MenuType menuType;

    private Long parentId;

    private String url;

    private String description;

    private Long boardId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReadResponse(Long menuId, String nameEng, String nameKor, Integer menuOrder,
        MenuType menuType, Long parentId, String url, String description, Long boardId,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.menuId = menuId;
      this.nameEng = nameEng;
      this.nameKor = nameKor;
      this.menuOrder = menuOrder;
      this.menuType = menuType;
      this.parentId = parentId;
      this.url = url;
      this.description = description;
      this.boardId = boardId;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }

    public static MenuReadDto.ReadResponse fromEntity(Menu menu) {
      ReadResponse.ReadResponseBuilder builder = ReadResponse.builder();

      if(menu.getBoard() != null)
        builder.boardId(menu.getBoard().getBoardId());

      return builder.menuId(menu.getMenuId())
          .nameEng(menu.getNameEng())
          .nameKor(menu.getNameKor())
          .menuOrder(menu.getMenuOrder())
          .description(menu.getDescription())
          .menuType(menu.getMenuType())
          .url(menu.getUrl())
          .parentId(getParent(menu))

          .createdAt(menu.getCreatedAt())
          .updatedAt(menu.getUpdatedAt())
          .build();
    }

    private static Long getParent(Menu menu) {
      if(menu.getParent() == null)
        return null;
      return menu.getParent().getMenuId();
    }
  }
}
