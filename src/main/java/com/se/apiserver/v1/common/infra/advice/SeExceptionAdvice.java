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

import com.se.apiserver.v1.common.infra.logging.SeLogger;
import com.se.apiserver.v1.common.infra.logging.standard.SeStandardLogger;
import com.se.apiserver.v1.common.presentation.response.ExceptionResponse;
import com.se.apiserver.v1.common.domain.exception.PreconditionFailedException;
import com.se.apiserver.v1.common.domain.exception.SeException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SeExceptionAdvice {

  private final SeLogger logger;

  public SeExceptionAdvice() {
    this.logger = new SeStandardLogger();
  }

  @ExceptionHandler(SeException.class)
  public ResponseEntity<ExceptionResponse> handleSeException(final SeException e) {
    this.countExceptionAndLog(e);
    if (e.getHttpStatus() == null)
      return new ResponseEntity<>(ExceptionResponse.of(e), HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(ExceptionResponse.of(e), e.getHttpStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<PreconditionFailedException> handleConstraintViolation(final ConstraintViolationException e) {
    this.countExceptionAndLog(e);
    return new ResponseEntity<>(
        new PreconditionFailedException(e.getMessage(), e),
        HttpStatus.PRECONDITION_FAILED
    );
  }

  private void countExceptionAndLog(final Exception e) {
    logger.error(e.getClass().getSimpleName(), e.getMessage());
    logger.debug(e.getClass().getSimpleName(), e.getMessage(), e);
  }
}
