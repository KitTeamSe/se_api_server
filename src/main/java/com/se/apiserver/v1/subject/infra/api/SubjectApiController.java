package com.se.apiserver.v1.subject.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.subject.domain.usecase.SubjectCreateUseCase;
import com.se.apiserver.v1.subject.domain.usecase.SubjectDeleteUseCase;
import com.se.apiserver.v1.subject.domain.usecase.SubjectReadUseCase;
import com.se.apiserver.v1.subject.domain.usecase.SubjectUpdateUseCase;
import com.se.apiserver.v1.subject.infra.dto.SubjectCreateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/administrator")
@Api(tags = "교과 관리")
public class SubjectApiController {

  private final SubjectCreateUseCase subjectCreateUseCase;
//  private final SubjectReadUseCase subjectReadUseCase;
//  private final SubjectUpdateUseCase subjectUpdateUseCase;
//  private final SubjectDeleteUseCase subjectDeleteUseCase;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/subject")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "교과 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated SubjectCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "교과 생성에 성공했습니다.", subjectCreateUseCase.create(request));
  }
}
