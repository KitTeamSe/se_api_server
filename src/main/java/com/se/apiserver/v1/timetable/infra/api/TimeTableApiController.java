package com.se.apiserver.v1.timetable.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableCreateUseCase;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableDeleteUseCase;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableReadUseCase;
import com.se.apiserver.v1.timetable.domain.usecase.TimeTableUpdateUseCase;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableCreateDto;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableReadDto;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableReadDto.Response;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableUpdateDto;
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
@Api(tags = "시간표 관리")
public class TimeTableApiController {

  private final TimeTableCreateUseCase timeTableCreateUseCase;
  private final TimeTableReadUseCase timeTableReadUseCase;
  private final TimeTableUpdateUseCase timeTableUpdateUseCase;
  private final TimeTableDeleteUseCase timeTableDeleteUseCase;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/time-table")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "시간표 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated TimeTableCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의실 생성에 성공했습니다.", timeTableCreateUseCase.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/time-table/{id}")
  @ApiOperation("시간표 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<TimeTableReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", timeTableReadUseCase.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/time-table")
  @ApiOperation("시간표 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", timeTableReadUseCase.readAll(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/time-table")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "시간표 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated
      TimeTableUpdateDto.Request request){

    return new SuccessResponse<>(HttpStatus.OK.value(), "시간표 수정에 성공했습니다.", timeTableUpdateUseCase.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/time-table/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "시간표 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    timeTableDeleteUseCase.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "시간표 삭제에 성공했습니다.");
  }

}
