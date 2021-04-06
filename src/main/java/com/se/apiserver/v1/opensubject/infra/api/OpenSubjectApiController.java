package com.se.apiserver.v1.opensubject.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectCreateDto;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectCreateService;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectDeleteService;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectReadService;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectUpdateService;
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
@Api(tags = "개설 교과 관리")
public class OpenSubjectApiController {

  private final OpenSubjectCreateService openSubjectCreateService;
//  private final OpenSubjectReadService openSubjectReadService;
//  private final OpenSubjectUpdateService openSubjectUpdateService;
//  private final OpenSubjectDeleteService openSubjectDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/open-subject")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "개설 교과 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated OpenSubjectCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "개설 교과 생성에 성공했습니다.", openSubjectCreateService
        .create(request));
  }

}
