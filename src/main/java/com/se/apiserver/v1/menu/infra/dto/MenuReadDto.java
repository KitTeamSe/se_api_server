package com.se.apiserver.v1.menu.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MenuReadDto {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class ReadAllResponse{

        private Long menuId;

        private String nameEng;

        private String nameKor;

        private Integer menuOrder;

        private String description;

        private List<ReadAllResponse> child;

        public static MenuReadDto.ReadAllResponse fromEntity(Menu menu, Set<String> authorities){
            MenuReadDto.ReadAllResponse.ReadAllResponseBuilder responseBuilder = MenuReadDto.ReadAllResponse.builder()
                    .menuId(menu.getMenuId())
                    .menuOrder(menu.getMenuOrder())
                    .description(menu.getDescription())
                    .nameEng(menu.getNameEng())
                    .nameKor(menu.getNameKor());

            List<ReadAllResponse> tmp = new ArrayList<>();
            for(Menu child : menu.getChild()){
                if(authorities.contains(child.getAuthority().getAuthority()))
                    tmp.add(fromEntity(child, authorities));
            }
            responseBuilder.child(tmp);
            return responseBuilder.build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class ReadResponse {
        private Long menuId;

        private String nameEng;

        private String nameKor;

        private Integer menuOrder;

        private String description;

        public static MenuReadDto.ReadResponse fromEntity(Menu menu){
            return ReadResponse.builder()
                    .menuId(menu.getMenuId())
                    .nameEng(menu.getNameEng())
                    .nameKor(menu.getNameKor())
                    .menuOrder(menu.getMenuOrder())
                    .description(menu.getDescription())
                    .build();
        }
    }
}
