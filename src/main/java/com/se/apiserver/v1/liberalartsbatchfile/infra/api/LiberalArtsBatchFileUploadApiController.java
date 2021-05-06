package com.se.apiserver.v1.liberalartsbatchfile.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.liberalartsbatchfile.application.service.LiberalArtsBatchFileDownloadService;
import com.se.apiserver.v1.liberalartsbatchfile.application.service.LiberalArtsBatchFileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/liberal-arts-batch-file")
@Api(tags = "교양 배치 파일 업로드")
public class LiberalArtsBatchFileUploadApiController {

  private final LiberalArtsBatchFileUploadService liberalArtsBatchfileUploadService;

  @PreAuthorize("hasAnyAuthority('SCHEDULE_MANAGE')")
  @PostMapping(path = "/upload")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "교양 배치 파일 업로드")
  public SuccessResponse upload(@PathVariable(value = "id") Long timeTableId, @RequestParam("file") MultipartFile file){
    liberalArtsBatchfileUploadService.upload(timeTableId, file);
    return new SuccessResponse(HttpStatus.OK.value(), "교양 배치 파일 업로드에 성공했습니다.");
  }
}
