package com.se.apiserver.v1.common.infra.advice;

/*
 *
 *  Copyright 2021 KitTeamSe.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  Original Code
 *  https://github.com/Netflix/genie/blob/master/genie-web/src/main/java/com/netflix/genie/web/apis/rest/v3/controllers/GenieExceptionMapper.java
 *  modified by dldhk97
 */

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.se.apiserver.v1.common.infra.logging.SeLogger;
import com.se.apiserver.v1.common.infra.logging.standard.SeStandardLogger;
import com.se.apiserver.v1.common.presentation.response.ExceptionResponse;
import com.se.apiserver.v1.common.domain.exception.PreconditionFailedException;
import com.se.apiserver.v1.common.domain.exception.SeException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.NestedServletException;

@RestControllerAdvice
public class SeExceptionAdvice {

  private final SeLogger logger;

  public SeExceptionAdvice() {
    this.logger = new SeStandardLogger();
  }

  @ExceptionHandler(SeException.class)
  public ResponseEntity<ExceptionResponse> handleSeException(final SeException e) {
    this.countExceptionAndLog(e);
    HttpStatus status = e.getHttpStatus();
    if (status == null)
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    return new ResponseEntity<>(ExceptionResponse.of(e), status);
  }

  // When precondition failed
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ExceptionResponse> handleConstraintViolationException(final ConstraintViolationException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e), HttpStatus.PRECONDITION_FAILED);
  }

  // When input is invalid value
  @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<ExceptionResponse> handleBindException(final BindException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e), HttpStatus.BAD_REQUEST);
  }

  // When method is not allowed
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e), HttpStatus.METHOD_NOT_ALLOWED);
  }

  // When unauthorized
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ExceptionResponse> handleAccessDeniedException(final AccessDeniedException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e), HttpStatus.UNAUTHORIZED);
  }

  // When json parse exception
  @ExceptionHandler(JsonParseException.class)
  protected ResponseEntity<ExceptionResponse> handleJsonParseException(final JsonParseException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e, "Json 파싱에 실패했습니다."), HttpStatus.BAD_REQUEST);
  }

  // When invalid enum input exception
  @ExceptionHandler(InvalidFormatException.class)
  protected ResponseEntity<ExceptionResponse> handleInvalidFormatException(final InvalidFormatException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e, "유효하지 않은 enum 입니다."), HttpStatus.BAD_REQUEST);
  }

  // When NestedServletException caused.
  @ExceptionHandler(NestedServletException.class)
  protected ResponseEntity<ExceptionResponse> handleInvalidFormatException(final NestedServletException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(ExceptionResponse.of(e, e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  private void countExceptionAndLog(final Exception e) {
    logger.error(e.getClass().getSimpleName(), e.getMessage());
    logger.debug(e.getClass().getSimpleName(), e.getMessage(), e);
  }
}