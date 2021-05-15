package com.se.apiserver.v1.participatedteacher.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherCreateService;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherDeleteService;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherReadService;
import com.se.apiserver.v1.participatedteacher.application.dto.ParticipatedTeacherCreateDto;
import com.se.apiserver.v1.participatedteacher.application.dto.ParticipatedTeacherReadDto;
import com.se.apiserver.v1.participatedteacher.application.dto.ParticipatedTeacherReadDto.Response;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/administrator")
@Api(tags = "참여 교원 관리")
public class ParticipatedTeacherApiController {

  private final ParticipatedTeacherCreateService participatedTeacherCreateService;
  private final ParticipatedTeacherReadService participatedTeacherReadService;
  private final ParticipatedTeacherDeleteService participatedTeacherDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/participated-teacher")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "참여 교원 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated ParticipatedTeacherCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "참여 교원 생성에 성공했습니다.", participatedTeacherCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/participated-teacher/{id}")
  @ApiOperation("참여 교원 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<ParticipatedTeacherReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", participatedTeacherReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/participated-teacher")
  @ApiOperation("시간표에 참여한 참여 교원 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest, Long timeTableId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", participatedTeacherReadService.readAllByTimeTableId(pageRequest.of(), timeTableId));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/participated-teacher/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "참여 교원 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    participatedTeacherDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "참여 교원 삭제에 성공했습니다.");
  }
}
