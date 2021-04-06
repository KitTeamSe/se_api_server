package com.se.apiserver.v1.lectureunabletime.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeCreateDto;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeUpdateDto;
import com.se.apiserver.v1.lectureunabletime.application.service.LectureUnableTimeCreateService;
import com.se.apiserver.v1.lectureunabletime.application.service.LectureUnableTimeDeleteService;
import com.se.apiserver.v1.lectureunabletime.application.service.LectureUnableTimeReadService;
import com.se.apiserver.v1.lectureunabletime.application.service.LectureUnableTimeUpdateService;
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
@Api(tags = "강의 불가 시간 관리")
public class LectureUnableTimeApiController {

  private final LectureUnableTimeCreateService lectureUnableTimeCreateService;
//  private final LectureUnableTimeReadService lectureUnableTimeReadService;
//  private final LectureUnableTimeUpdateService lectureUnableTimeUpdateService;
//  private final LectureUnableTimeDeleteService lectureUnableTimeDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/lecture-unable-time")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "강의 불가 시간 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated LectureUnableTimeCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의 불가 시간 생성에 성공했습니다.", lectureUnableTimeCreateService.create(request));
  }

}
