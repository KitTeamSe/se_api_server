package com.se.apiserver.v1.opensubject.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectCreateDto;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectReadDto;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectReadDto.Response;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectUpdateDto;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectCreateService;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectDeleteService;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectReadService;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectUpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  private final OpenSubjectReadService openSubjectReadService;
  private final OpenSubjectUpdateService openSubjectUpdateService;
//  private final OpenSubjectDeleteService openSubjectDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/open-subject")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "개설 교과 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated OpenSubjectCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "개설 교과 생성에 성공했습니다.", openSubjectCreateService
        .create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/open-subject/{id}")
  @ApiOperation("개설 교과 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<OpenSubjectReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", openSubjectReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/open-subject")
  @ApiOperation("시간표에 추가된 개설 교과 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest, Long timeTableId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", openSubjectReadService.readAllByTimeTableId(pageRequest.of(), timeTableId));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/open-subject")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "개설 교과 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated
      OpenSubjectUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "개설 교과 수정에 성공했습니다.", openSubjectUpdateService.update(request));
  }

}
