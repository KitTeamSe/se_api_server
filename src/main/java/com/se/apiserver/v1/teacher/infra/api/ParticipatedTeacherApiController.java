package com.se.apiserver.v1.teacher.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.teacher.domain.usecase.participatedteacher.ParticipatedTeacherCreateUseCase;
import com.se.apiserver.v1.teacher.domain.usecase.participatedteacher.ParticipatedTeacherDeleteUseCase;
import com.se.apiserver.v1.teacher.domain.usecase.participatedteacher.ParticipatedTeacherReadUseCase;
import com.se.apiserver.v1.teacher.infra.dto.participatedteacher.ParticipatedTeacherCreateDto;
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
@Api(tags = "참여 교원 관리")
public class ParticipatedTeacherApiController {

  private final ParticipatedTeacherCreateUseCase participatedTeacherCreateUseCase;
//  private final ParticipatedTeacherReadUseCase participatedTeacherReadUseCase;
//  private final ParticipatedTeacherDeleteUseCase participatedTeacherDeleteUseCase;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/participated-teacher")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "참여 교원 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated ParticipatedTeacherCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "참여 교원 생성에 성공했습니다.", participatedTeacherCreateUseCase.create(request));
  }
}
