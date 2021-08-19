package com.se.apiserver.v1.common.infra.logging;

/**
 * 로그 스택을 용이하게 변경하기 위한 로거 인터페이스
 * 로그 레벨에 따라 FATAL > ERROR >WARN > INFO > DEBUG > TRACE 순으로 로깅됩니다.
 * 만약 디버그 레벨이 DEBUG 라면 FATAL ~ DEBUG 까지 모두 로깅이 됩니다.
 */
public interface SeLogger {

  // 요청을 처리하는 중 문제가 발생한 상태.
  void error(String className, String msg);
  void error(String className, String msg, Throwable t);

  // 처리 가능한 문제이지만, 향후 에러의 원인이 될 수 있음.
  void warn(String className, String msg);
  void warn(String className, String msg, Throwable t);

  // 로그인, 상태 변경과 같은 정보성 메시지 표시.
  void info(String className, String msg);
  void info(String className, String msg, Throwable t);

  // 개발 시 디버그 용도로 사용.
  void debug(String className, String msg);
  void debug(String className, String msg, Throwable t);
}
