package com.se.apiserver.v1.subject.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.subject.application.service.SubjectCreateService;
import com.se.apiserver.v1.subject.application.service.SubjectDeleteService;
import com.se.apiserver.v1.subject.application.service.SubjectReadService;
import com.se.apiserver.v1.subject.application.service.SubjectUpdateService;
import com.se.apiserver.v1.subject.application.dto.SubjectCreateDto;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto.Response;
import com.se.apiserver.v1.subject.application.dto.SubjectUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Api(tags = "교과 관리")
public class SubjectApiController {

  private final SubjectCreateService subjectCreateService;
  private final SubjectReadService subjectReadService;
  private final SubjectUpdateService subjectUpdateService;
  private final SubjectDeleteService subjectDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/subject")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "교과 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated SubjectCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "교과 생성에 성공했습니다.", subjectCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/subject/{id}")
  @ApiOperation("교과 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<SubjectReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", subjectReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/subject")
  @ApiOperation("교과 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", subjectReadService.readAll(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/subject")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "교과 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated SubjectUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "교과 수정에 성공했습니다.", subjectUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/subject/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "교과 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    subjectDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "교과 삭제에 성공했습니다.");
  }
}
