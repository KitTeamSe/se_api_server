package com.se.apiserver.v1.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class MenuUpdateDto {

    @ApiModel("메뉴 수정 요청")
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class Request{

        @NotNull
        @ApiModelProperty(notes = "메뉴 pk", example = "1")
        private Long menuId;

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "영어명, 링크로 사용될 것임", example = "freeboard")
        private String nameEng;

        @Size(min = 2, max = 20)
        @ApiModelProperty(notes = "한글명, 실제 출력", example = "자유게시판")
        private String nameKor;

        @Size(max = 255)
        @ApiModelProperty(notes = "url", example = "freeboard")
        private String url;

        @ApiModelProperty(notes = "메뉴 출력 순서", example = "1")
        private Integer menuOrder;

        @Size(max = 50)
        @ApiModelProperty(notes = "설명", example = "자유게시판 입니다.")
        private String description;

        @ApiModelProperty(notes = "상위 메뉴 pk, null 가능", example = "2")
        private Long parentId;

        public Request(Long menuId, String nameEng, String nameKor, String url, Integer menuOrder,
            String description, Long parentId) {
            this.menuId = menuId;
            this.nameEng = nameEng;
            this.nameKor = nameKor;
            this.url = url;
            this.menuOrder = menuOrder;
            this.description = description;
            this.parentId = parentId;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{

        private Long menuId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long parentId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String parentNameEng;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String parentNameKor;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String parentDescription;

        private String nameEng;

        private String nameKor;

        private Integer menuOrder;

        private String description;

        public static Response fromEntity(Menu menu){
            ResponseBuilder responseBuilder = Response.builder()
                    .menuId(menu.getMenuId())
                    .menuOrder(menu.getMenuOrder())
                    .description(menu.getDescription())
                    .nameEng(menu.getNameEng())
                    .nameKor(menu.getNameKor());
//            if(menu.getParent() != null){
//                responseBuilder
//                        .parentId(menu.getParent().getMenuId())
//                        .parentDescription(menu.getParent().getDescription())
//                        .parentNameEng(menu.getParent().getNameEng())
//                        .parentNameKor(menu.getParent().getNameKor());
//            }

            return responseBuilder.build();
        }
    }

}
