package com.se.apiserver.v1.post.domain.entity;

public enum PostSearchType {
  TITLE_TEXT,   // 제목 + 내용
  TITLE,        // 제목
  TEXT,         // 내용
  REPLY,        // 댓글
  NICKNAME,     // 닉네임
  USERID,       // 사용자 ID
  TAG;          // 태그
}
