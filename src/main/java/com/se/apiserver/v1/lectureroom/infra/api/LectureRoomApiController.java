package com.se.apiserver.v1.lectureroom.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.lectureroom.application.service.LectureRoomCreateService;
import com.se.apiserver.v1.lectureroom.application.service.LectureRoomDeleteService;
import com.se.apiserver.v1.lectureroom.application.service.LectureRoomReadService;
import com.se.apiserver.v1.lectureroom.application.service.LectureRoomUpdateService;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomCreateDto;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomUpdateDto;
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
@Api(tags = "강의실 관리")
public class LectureRoomApiController {

  private final LectureRoomCreateService lectureRoomCreateService;
  private final LectureRoomReadService lectureRoomReadService;
  private final LectureRoomUpdateService lectureRoomUpdateService;
  private final LectureRoomDeleteService lectureRoomDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/lecture-room")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "강의실 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated LectureRoomCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의실 생성에 성공했습니다.", lectureRoomCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/lecture-room/{id}")
  @ApiOperation("강의실 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<LectureRoomReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", lectureRoomReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/lecture-room")
  @ApiOperation("강의실 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<LectureRoomReadDto.Response>> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", lectureRoomReadService.readAll(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/lecture-room")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "강의실 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated
      LectureRoomUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "강의실 수정에 성공했습니다.", lectureRoomUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/lecture-room/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "강의실 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    lectureRoomDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "강의실 삭제에 성공했습니다.");
  }


}
