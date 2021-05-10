package com.se.apiserver.v1.multipartfile.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDeleteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "multipart-file/delete")
@Api(tags = "파일 삭제")
public class MultipartFileDeleteController {

  @Autowired
  private MultipartFileDeleteService multipartFileDeleteService;

  @GetMapping("/{saveName:.+}")
  @ApiOperation("단일 파일 삭제")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse downloadFile(@PathVariable String saveName) {
    multipartFileDeleteService.delete(saveName);
    return new SuccessResponse<>(HttpStatus.OK.value(), "파일 삭제에 성공했습니다.");
  }
}
