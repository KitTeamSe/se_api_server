package com.se.apiserver.v1.usablelectureroom.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomCreateDto;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomReadDto;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomReadDto.Response;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomCreateService;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomDeleteService;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomReadService;
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
@Api(tags = "사용 가능 강의실 관리")
public class UsableLectureRoomApiController {
  
  private final UsableLectureRoomCreateService usableLectureRoomCreateService;
  private final UsableLectureRoomReadService usableLectureRoomReadService;
  private final UsableLectureRoomDeleteService usableLectureRoomDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/usable-lecture-room")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "사용 가능 강의실 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated UsableLectureRoomCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "사용 가능 강의실 생성에 성공했습니다.", usableLectureRoomCreateService
        .create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/usable-lecture-room/{id}")
  @ApiOperation("사용 가능 강의실 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<UsableLectureRoomReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", usableLectureRoomReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/usable-lecture-room")
  @ApiOperation("시간표에 추가된 모든 사용 가능 강의실 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest, Long timeTableId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", usableLectureRoomReadService.readAllByTimeTableId(pageRequest.of(), timeTableId));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/usable-lecture-room/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "사용 가능 강의실 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    usableLectureRoomDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "사용 가능 강의실 삭제에 성공했습니다.");
  }

}
