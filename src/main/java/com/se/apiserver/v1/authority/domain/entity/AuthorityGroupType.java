package com.se.apiserver.v1.authority.domain.entity;

public enum AuthorityGroupType {
    SYSTEM,             // 시스템 사용자 그룹(ADMIN)
    DEFAULT,            // 기본 사용자 그룹(회원가입 시 자동 할당)
    NORMAL,             // 임의 사용자 그룹(생성 및 삭제가능)
    ANONYMOUS           // 익명 사용자 그룹(JWT 기본 할당)
}
