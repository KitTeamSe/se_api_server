package com.se.apiserver.v1.deployment.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.application.dto.DeploymentReadDto;
import com.se.apiserver.v1.deployment.application.dto.DeploymentReadDto.Response;
import com.se.apiserver.v1.deployment.application.service.DeploymentCreateService;
import com.se.apiserver.v1.deployment.application.service.DeploymentDeleteService;
import com.se.apiserver.v1.deployment.application.service.DeploymentReadService;
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
@Api(tags = "배치 관리")
public class DeploymentApiController {

  private final DeploymentCreateService deploymentCreateService;
  private final DeploymentReadService deploymentReadService;
  private final DeploymentDeleteService deploymentDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/deployment")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "배치 추가")
  public SuccessResponse<DeploymentCreateDto.Resposne> create(@RequestBody @Validated DeploymentCreateDto.Request request){
    return new SuccessResponse<DeploymentCreateDto.Resposne>(HttpStatus.CREATED.value(), "배치 생성에 성공했습니다.", deploymentCreateService
        .create(request));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/deployment/{id}")
  @ApiOperation("배치 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<DeploymentReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", deploymentReadService.read(id));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @GetMapping(path = "/deployment")
  @ApiOperation("시간표에 추가된 배치 조회")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<PageImpl<Response>> readAll(@Validated PageRequest pageRequest, Long timeTableId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", deploymentReadService.readAllByTimeTableId(pageRequest.of(), timeTableId));
  }

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @DeleteMapping(path = "/deployment/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "배치 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    deploymentDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "배치 삭제에 성공했습니다.");
  }
}
