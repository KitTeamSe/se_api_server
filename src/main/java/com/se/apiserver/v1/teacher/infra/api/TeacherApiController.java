package com.se.apiserver.v1.teacher.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.teacher.application.service.TeacherCreateService;
import com.se.apiserver.v1.teacher.application.service.TeacherDeleteService;
import com.se.apiserver.v1.teacher.application.service.TeacherReadService;
import com.se.apiserver.v1.teacher.application.service.TeacherUpdateService;
import com.se.apiserver.v1.teacher.application.dto.TeacherCreateDto;
import com.se.apiserver.v1.teacher.application.dto.TeacherReadDto;
import com.se.apiserver.v1.teacher.application.dto.TeacherUpdateDto;
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
@Api(tags = "교원 관리")
public class TeacherApiController {

  private final TeacherCreateService teacherCreateService;
  private final TeacherReadService teacherReadService;
  private final TeacherUpdateService teacherUpdateService;
  private final TeacherDeleteService teacherDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/teacher")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "교원 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated TeacherCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "교원 생성에 성공했습니다.", teacherCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/teacher/{id}")
  @ApiOperation("교원 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<TeacherReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", teacherReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/teacher")
  @ApiOperation("교원 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<TeacherReadDto.Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", teacherReadService.readAll(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/teacher")
  @ApiOperation("교원 수정")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<TeacherReadDto.Response> update(@RequestBody @Validated TeacherUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "교원 수정에 성공했습니다.", teacherUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/teacher/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "교원 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    teacherDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "교원 삭제에 성공했습니다.");
  }
}
