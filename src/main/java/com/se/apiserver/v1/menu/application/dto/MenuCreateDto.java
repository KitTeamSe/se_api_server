package com.se.apiserver.v1.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MenuCreateDto {

    @ApiModel("메뉴 등록 요청")
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class Request{

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "영어명, 링크로 사용될 것", example = "freeboard")
        @NotEmpty
        private String nameEng;

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "한글명, 실제 출력", example = "자유게시판")
        @NotEmpty
        private String nameKor;

        @Size(min = 2, max = 255)
        @ApiModelProperty(notes = "url", example = "freeboard")
        private String url;

        @ApiModelProperty(notes = "메뉴 출력 순서", example = "1")
        private Integer menuOrder;

        @Size(min = 2, max = 255)
        @ApiModelProperty(notes = "설명", example = "자유게시판 입니다.")
        private String description;

        private MenuType menuType;

        @ApiModelProperty(notes = "상위 메뉴 pk, null 가능", example = "1")
        private Long parentId;

        public Request(String nameEng, String nameKor, String url, Integer menuOrder,
            String description, MenuType menuType, Long parentId) {
            this.nameEng = nameEng;
            this.nameKor = nameKor;
            this.url = url;
            this.menuOrder = menuOrder;
            this.description = description;
            this.menuType = menuType;
            this.parentId = parentId;
        }
    }

    @ApiModel("메뉴 등록 응답")
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class Response{

        private Long menuId;

        private String nameEng;

        private String nameKor;

        private String url;

        private Integer menuOrder;

        private String description;

        private MenuType menuType;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long parentId;

        public Response(Long menuId, String nameEng, String nameKor, String url,
            Integer menuOrder, String description, MenuType menuType, Long parentId) {
            this.menuId = menuId;
            this.nameEng = nameEng;
            this.nameKor = nameKor;
            this.url = url;
            this.menuOrder = menuOrder;
            this.description = description;
            this.menuType = menuType;
            this.parentId = parentId;
        }

        public static Response fromEntity(Menu menu) {
            ResponseBuilder responseBuilder = Response.builder();
            responseBuilder.nameKor(menu.getNameKor())
                    .menuId(menu.getMenuId())
                    .url(menu.getUrl())
                    .menuType(menu.getMenuType())
                    .nameEng(menu.getNameEng())
                    .menuOrder(menu.getMenuOrder())
                    .description(menu.getDescription());
            if(menu.getParent() != null)
                responseBuilder.parentId(menu.getParent().getMenuId());
            return responseBuilder.build();
        }
    }
}
