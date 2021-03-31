package com.se.apiserver.v1.period.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.period.domain.usecase.PeriodCreateUseCase;
import com.se.apiserver.v1.period.domain.usecase.PeriodDeleteUseCase;
import com.se.apiserver.v1.period.domain.usecase.PeriodReadUseCase;
import com.se.apiserver.v1.period.domain.usecase.PeriodUpdateUseCase;
import com.se.apiserver.v1.period.infra.dto.PeriodCreateDto;
import com.se.apiserver.v1.period.infra.dto.PeriodReadDto;
import com.se.apiserver.v1.period.infra.dto.PeriodReadDto.Response;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/administrator")
@Api(tags = "교시 관리")
public class PeriodApiController {

  private final PeriodCreateUseCase periodCreateUseCase;
  private final PeriodReadUseCase periodReadUseCase;
//  private final PeriodUpdateUseCase periodUpdateUseCase;
//  private final PeriodDeleteUseCase periodDeleteUseCase;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/period")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "교시 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated PeriodCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "교시 생성에 성공했습니다.", periodCreateUseCase.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/period/{id}")
  @ApiOperation("교시 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PeriodReadDto.Response> read(@PathVariable(value = "id") Long id) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", periodReadUseCase.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/period")
  @ApiOperation("교시 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", periodReadUseCase.readAll(pageRequest.of()));
  }
}
