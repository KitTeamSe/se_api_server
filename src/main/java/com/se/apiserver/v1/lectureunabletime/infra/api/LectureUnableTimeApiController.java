package com.se.apiserver.v1.lectureunabletime.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeCreateDto;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeReadDto;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeReadDto.Response;
import com.se.apiserver.v1.lectureunabletime.application.service.LectureUnableTimeCreateService;
import com.se.apiserver.v1.lectureunabletime.application.service.LectureUnableTimeReadService;
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
@Api(tags = "강의 불가 시간 관리")
public class LectureUnableTimeApiController {

  private final LectureUnableTimeCreateService lectureUnableTimeCreateService;
  private final LectureUnableTimeReadService lectureUnableTimeReadService;
//  private final LectureUnableTimeUpdateService lectureUnableTimeUpdateService;
//  private final LectureUnableTimeDeleteService lectureUnableTimeDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/lecture-unable-time")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "강의 불가 시간 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated LectureUnableTimeCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의 불가 시간 생성에 성공했습니다.", lectureUnableTimeCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/lecture-unable-time/{id}")
  @ApiOperation("강의 불가 시간 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<LectureUnableTimeReadDto.Response> read(@PathVariable(value = "id") Long id) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", lectureUnableTimeReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/lecture-unable-time")
  @ApiOperation("강의 불가 시간 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest, Long participatedTeacherId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", lectureUnableTimeReadService.readAllByParticipatedTeacher(pageRequest.of(), participatedTeacherId));
  }

}
