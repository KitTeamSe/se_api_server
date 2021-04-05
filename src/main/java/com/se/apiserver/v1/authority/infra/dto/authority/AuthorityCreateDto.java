package com.se.apiserver.v1.authority.infra.dto.authority;

import com.se.apiserver.v1.menu.domain.entity.Menu;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

public class AuthorityCreateDto {

    @Builder
    @Data
    static public class Request{
        @Size(min = 2, max = 20)
        private String nameEng;

        @Size(min = 2, max = 20)
        private String nameKor;
    }
}
