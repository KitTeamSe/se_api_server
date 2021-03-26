package com.se.apiserver.v1.board.infra.dto;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

public class BoardReadDto {

    @Data
    @AllArgsConstructor
    @Builder
    static public class Request{

        @Size(min = 2, max = 20)
        String nameEng;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static public class ReadResponse{
        private Long boardId;

        private String nameEng;

        private String nameKor;

        private String menuNameKor;

        private String menuNameEng;

        static public ReadResponse fromEntity(Board board){
            ReadResponseBuilder readResponseBuilder = ReadResponse.builder()
                    .boardId(board.getBoardId())
                    .nameEng(board.getNameEng())
                    .nameKor(board.getNameKor());
            if(board.getMenu() == null)
                return readResponseBuilder.build();

            readResponseBuilder
                    .menuNameEng(board.getMenu().getNameEng())
                    .menuNameKor(board.getMenu().getNameKor());
            return readResponseBuilder.build();
        }
    }
}
