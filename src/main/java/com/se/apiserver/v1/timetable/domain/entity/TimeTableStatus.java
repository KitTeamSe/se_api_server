package com.se.apiserver.v1.timetable.domain.entity;

public enum TimeTableStatus {
  CREATED,                       // 생성됨
  SELECT_LECTURE_ROOM,            // 강의실 선택
  SELECT_TEACHER,                 // 교원 선택
  SELECT_SUBJECT,                 // 교과 선택
  INSERT_LECTURE_UNABLE_TIME,     // 강의 불가 시간 입력
  INSERT_OPEN_SUBJECT,            // 개설 교과 입력
  INSERT_LIBERAL_ARTS,            // 교양 입력
  PLACE_SUBJECT,                  // 교과 배치
  FINAL_REVIEW,                   // 최종 검토
  FINISHED                        // 완료
}
