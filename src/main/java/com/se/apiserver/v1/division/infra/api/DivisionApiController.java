package com.se.apiserver.v1.division.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.division.application.dto.DivisionCheckDto;
import com.se.apiserver.v1.division.application.dto.DivisionCreateDto;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto.Response;
import com.se.apiserver.v1.division.application.dto.DivisionUpdateDto;
import com.se.apiserver.v1.division.application.service.DivisionCheckService;
import com.se.apiserver.v1.division.application.service.DivisionCreateService;
import com.se.apiserver.v1.division.application.service.DivisionDeleteService;
import com.se.apiserver.v1.division.application.service.DivisionReadService;
import com.se.apiserver.v1.division.application.service.DivisionUpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@Api(tags = "분반 관리")
public class DivisionApiController {

  private final DivisionCreateService divisionCreateService;
  private final DivisionReadService divisionReadService;
  private final DivisionDeleteService divisionDeleteService;
  private final DivisionCheckService divisionCheckService;
  private final DivisionUpdateService divisionUpdateService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/division")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "분반 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated DivisionCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "분반 생성에 성공했습니다.", divisionCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/division/{id}")
  @ApiOperation("분반 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<DivisionReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", divisionReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/division")
  @ApiOperation("개설 교과에 추가된 분반 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<List<Response>> readAll(Long openSubjectId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", divisionReadService.readAllByOpenSubjectId(openSubjectId));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PutMapping(path = "/division")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "분반 수정")
  public SuccessResponse<Long> update(@RequestBody @Validated DivisionUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "분반 수정에 성공했습니다.", divisionUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/division/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "분반 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    divisionDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "분반 삭제에 성공했습니다.");
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/division/check/{id}")
  @ApiOperation("분반 배치 확인")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<DivisionCheckDto.Response> check(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "해당 분반의 배치를 확인했습니다.", divisionCheckService.check(id));
  }
}
