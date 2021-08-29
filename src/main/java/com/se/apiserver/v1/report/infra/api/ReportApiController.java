package com.se.apiserver.v1.report.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.report.application.dto.ReportCreateDto;
import com.se.apiserver.v1.report.application.dto.ReportReadDto;
import com.se.apiserver.v1.report.application.dto.ReportReadDto.ReportSearchRequest;
import com.se.apiserver.v1.report.application.dto.ReportReadDto.Response;
import com.se.apiserver.v1.report.application.dto.ReportUpdateDto;
import com.se.apiserver.v1.report.application.service.ReportCreateService;
import com.se.apiserver.v1.report.application.service.ReportDeleteService;
import com.se.apiserver.v1.report.application.service.ReportReadService;
import com.se.apiserver.v1.report.application.service.ReportUpdateService;
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
@Api(tags = "신고 관리")
public class ReportApiController {

  private final ReportCreateService reportCreateService;
  private final ReportReadService reportReadService;
  private final ReportUpdateService reportUpdateService;
  private final ReportDeleteService reportDeleteService;

  @PostMapping(path = "/report")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "신고 추가")
  public SuccessResponse<Long> create(@RequestBody @Validated ReportCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "신고 생성에 성공했습니다.", reportCreateService.create(request));
  }

  @PreAuthorize("hasAnyAuthority('ACCESS_MANAGE')")
  @GetMapping(path = "/report/{id}")
  @ApiOperation("신고 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<ReportReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", reportReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('ACCESS_MANAGE')")
  @PostMapping(path = "/report/search")
  @ApiOperation("신고 전체 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@RequestBody @Validated ReportSearchRequest request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", reportReadService.readAll(request));
  }

  @GetMapping(path = "/report/my")
  @ApiOperation("My 신고 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readOwn(@Validated PageRequest pageRequest) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 내가 작성한 신고가 조회되었습니다.", reportReadService.readOwn(pageRequest.of()));
  }

  @PreAuthorize("hasAnyAuthority('ACCESS_MANAGE')")
  @PutMapping(path = "/report")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "신고 처리")
  public SuccessResponse<Long> update(@RequestBody @Validated ReportUpdateDto.Request request){
    return new SuccessResponse<>(HttpStatus.OK.value(), "신고 수정에 성공했습니다.", reportUpdateService.update(request));
  }

  @PreAuthorize("hasAnyAuthority('ACCESS_MANAGE')")
  @DeleteMapping(path = "/report/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "신고 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    reportDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "신고 삭제에 성공했습니다.");
  }

}
