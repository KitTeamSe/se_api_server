package com.se.apiserver.v1.deployment.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.application.service.DeploymentCreateService;
import com.se.apiserver.v1.deployment.application.service.DeploymentDeleteService;
import com.se.apiserver.v1.deployment.application.service.DeploymentReadService;
import com.se.apiserver.v1.deployment.application.service.DeploymentUpdateService;
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
@Api(tags = "배치 관리")
public class DeploymentApiController {

  private final DeploymentCreateService deploymentCreateService;
//  private final DeploymentReadService deploymentReadService;
//  private final DeploymentUpdateService deploymentUpdateService;
//  private final DeploymentDeleteService deploymentDeleteService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/deploy")
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "배치 추가")
  public SuccessResponse<DeploymentCreateDto.Resposne> create(@RequestBody @Validated DeploymentCreateDto.Request request){
    return new SuccessResponse<DeploymentCreateDto.Resposne>(HttpStatus.CREATED.value(), "배치 생성에 성공했습니다.", deploymentCreateService
        .create(request));
  }
}
