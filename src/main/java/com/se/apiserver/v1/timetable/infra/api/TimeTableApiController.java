package com.se.apiserver.v1.timetable.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateService;
import com.se.apiserver.v1.timetable.application.service.TimeTableDeleteService;
import com.se.apiserver.v1.timetable.application.service.TimeTableReadService;
import com.se.apiserver.v1.timetable.application.service.TimeTableUpdateService;
import com.se.apiserver.v1.timetable.application.dto.TimeTableCreateDto;
import com.se.apiserver.v1.timetable.application.dto.TimeTableReadDto;
import com.se.apiserver.v1.timetable.application.dto.TimeTableReadDto.Response;
import com.se.apiserver.v1.timetable.application.dto.TimeTableUpdateDto;
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

  private final TimeTableCreateService timeTableCreateService;
  private final TimeTableReadService timeTableReadService;
  private final TimeTableUpdateService timeTableUpdateService;
  private final TimeTableDeleteService timeTableDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/time-table")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "시간표 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated TimeTableCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "시간표 생성에 성공했습니다.", timeTableCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/time-table/{id}")
  @ApiOperation("시간표 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<TimeTableReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", timeTableReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/time-table")
  @ApiOperation("시간표 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", timeTableReadService.readAll(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/time-table")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "시간표 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated
      TimeTableUpdateDto.Request request){

    return new SuccessResponse<>(HttpStatus.OK.value(), "시간표 수정에 성공했습니다.", timeTableUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/time-table/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "시간표 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    timeTableDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "시간표 삭제에 성공했습니다.");
  }

}
