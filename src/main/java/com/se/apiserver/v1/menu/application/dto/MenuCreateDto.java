package com.se.apiserver.v1.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class MenuCreateDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("메뉴 등록 요청")
    static public class Request{

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "영어명, 링크로 사용될거임", example = "freeboard")
        @NotEmpty
        private String nameEng;

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "한글명, 실제 출력", example = "자유게시판")
        @NotEmpty
        private String nameKor;

        @Size(max = 255)
        @ApiModelProperty(notes = "url", example = "freeboard")
        @NotEmpty
        private String url;

        @ApiModelProperty(notes = "메뉴 출력 순서", example = "1")
        private Integer menuOrder;

        @Size(max = 255)
        @ApiModelProperty(notes = "설명", example = "자유게시판 입니다.")
        @NotEmpty
        private String description;

        private MenuType menuType;

        @ApiModelProperty(notes = "상위 메뉴 pk, null 가능", example = "1")
        private Long parentId;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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
