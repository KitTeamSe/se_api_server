package com.se.apiserver.v1.timetable.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableCreateUseCase;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableDeleteUseCase;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableReadUseCase;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableUpdateUseCase;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableCreateDto;
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
@Api(tags = "시간표 관리")
public class TimeTableApiController {

  private final TimeTableCreateUseCase timeTableCreateUseCase;
//  private final TimeTableReadUseCase timeTableReadUseCase;
//  private final TimeTableUpdateUseCase timeTableUpdateUseCase;
//  private final TimeTableDeleteUseCase timeTableDeleteUseCase;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/time-table")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "시간표 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated TimeTableCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의실 생성에 성공했습니다.", timeTableCreateUseCase.create(request));
  }

}
