package com.se.apiserver.v1.lectureroom.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomCreateUseCase;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomDeleteUseCase;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomReadUseCase;
import com.se.apiserver.v1.lectureroom.domain.usecase.LectureRoomUpdateUseCase;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomCreateDto;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomDeleteDto;
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
@Api(tags = "강의실 관리")
public class LectureRoomApiController {

  private final LectureRoomCreateUseCase lectureRoomCreateUseCase;
  private final LectureRoomReadUseCase lectureRoomReadUseCase;
//  private final LectureRoomUpdateUseCase lectureRoomUpdateUseCase;
  private final LectureRoomDeleteUseCase lectureRoomDeleteUseCase;

//  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/lecture-room")
  @ApiOperation("강의실 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest){
    return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", lectureRoomReadUseCase.readAll(pageRequest.of()));
  }

//  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/lecture-room")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "강의실 추가")
  public SuccessResponse<Long> createLectureRoom(@RequestBody @Validated LectureRoomCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "강의실 생성에 성공했습니다.", lectureRoomCreateUseCase.create(request));
  }

//  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/lecture-room/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "강의실 삭제")
  public SuccessResponse deleteLectureRoom(@PathVariable(value = "id") Long id){
    lectureRoomDeleteUseCase.delete(id);
    return new SuccessResponse(HttpStatus.OK.value(), "강의실 삭제에 성공했습니다.");
  }


}
