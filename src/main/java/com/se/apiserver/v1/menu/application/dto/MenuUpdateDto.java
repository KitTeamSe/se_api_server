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

}
