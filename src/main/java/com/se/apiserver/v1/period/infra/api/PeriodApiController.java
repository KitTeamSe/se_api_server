package com.se.apiserver.v1.period.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.period.application.service.PeriodCreateService;
import com.se.apiserver.v1.period.application.service.PeriodDeleteService;
import com.se.apiserver.v1.period.application.service.PeriodReadService;
import com.se.apiserver.v1.period.application.service.PeriodUpdateService;
import com.se.apiserver.v1.period.application.dto.PeriodCreateDto;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto.Response;
import com.se.apiserver.v1.period.application.dto.PeriodUpdateDto;
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
@Api(tags = "교시 관리")
public class PeriodApiController {

  private final PeriodCreateService periodCreateService;
  private final PeriodReadService periodReadService;
  private final PeriodUpdateService periodUpdateService;
  private final PeriodDeleteService periodDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/period")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "교시 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated PeriodCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "교시 생성에 성공했습니다.", periodCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/period/{id}")
  @ApiOperation("교시 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PeriodReadDto.Response> read(@PathVariable(value = "id") Long id) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", periodReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/period")
  @ApiOperation("교시 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", periodReadService.readAll(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/period")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "교시 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated
      PeriodUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "교시 수정에 성공했습니다.", periodUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/period/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "교시 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    periodDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "교시 삭제에 성공했습니다.");
  }
}
