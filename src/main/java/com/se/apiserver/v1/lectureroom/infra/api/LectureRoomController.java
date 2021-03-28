package com.se.apiserver.v1.lectureroom.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomCreateUseCase;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomDeleteUseCase;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomReadUseCase;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomUpdateUseCase;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomCreateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/administrator/lecture-room")
@Api(tags = "강의실 관리")
public class LectureRoomController {

  private final LectureRoomCreateUseCase lectureRoomCreateUseCase;
//  private final LectureRoomReadUseCase lectureRoomReadUseCase;
//  private final LectureRoomUpdateUseCase lectureRoomUpdateUseCase;
//  private final LectureRoomDeleteUseCase lectureRoomDeleteUseCase;

  @PostMapping(path = "/")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "강의실 추가")
  public SuccessResponse<Long> createLectureRoom(@RequestBody @Validated LectureRoomCreateDto.Request request, HttpServletRequest httpServletRequest){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의실 생성에 성공했습니다.", lectureRoomCreateUseCase.create(request, httpServletRequest.getRemoteAddr()));
  }
}
