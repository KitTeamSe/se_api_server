package com.se.apiserver.v1.usablelectureroom.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomCreateDto;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomCreateService;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomDeleteService;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomReadService;
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
@Api(tags = "사용 가능 강의실 관리")
public class UsableLectureRoomApiController {
  
  private final UsableLectureRoomCreateService usableLectureRoomCreateService;
//  private final UsableLectureRoomReadService usableLectureRoomReadService;
//  private final UsableLectureRoomDeleteService usableLectureRoomDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/usable-lecture-room")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "사용 가능 강의실 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated UsableLectureRoomCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "사용 가능 강의실 생성에 성공했습니다.", usableLectureRoomCreateService
        .create(request));
  }

}
