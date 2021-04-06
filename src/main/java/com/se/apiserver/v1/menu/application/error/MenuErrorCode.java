package com.se.apiserver.v1.menu.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MenuErrorCode implements ErrorCode {
    NO_SUCH_MENU(400, "MN01", "존재하지 않는 메뉴"),
    DUPLICATED_MENU_NAME_KOR(401, "MN02", "메뉴 한국명 중복"),
    DUPLICATED_MENU_NAME_ENG(402, "MN03", "메뉴 영문명 중복"),
    OCCUR_CYCLE(403,"MN04","사이클 발생"),
    CHILD_REMOVE_FIRST(404, "MN05", "하위 메뉴가 존재"),
    DUPLICATED_URL(405, "MN06", "url 중복"), 
    ONLY_FOLDER_MENU_CAN_BE_PARENT(406,"MN07" , "폴더 메뉴만 하위 메뉴를 가질 수 있습니다"),
    CAN_NOT_USE_NAME_ENG(406,"MN07" , "사용할 수 없는 영문명"),
    CAN_NOT_USE_NAME_KOR(406,"MN07" , "사용할 수 없는 한국명");

    private int status;
    private final String code;
    private final String message;

    MenuErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}