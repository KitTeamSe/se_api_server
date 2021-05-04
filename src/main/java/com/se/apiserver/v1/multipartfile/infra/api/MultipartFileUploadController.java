package com.se.apiserver.v1.multipartfile.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "multipart-file/upload")
@Api(tags = "파일 업로드")
public class MultipartFileUploadController {

  @Autowired
  private MultipartFileUploadService multipartFileUploadService;

  @PostMapping("/single/")
  @ApiOperation("단일 파일 업로드")
  @ResponseStatus(value = HttpStatus.OK)
  public SuccessResponse<String> uploadFile(@RequestParam("file") MultipartFile file){
    // 파일 서버에 전송.
    return new SuccessResponse<>(HttpStatus.OK.value(), "파일 업로드에 성공했습니다.", multipartFileUploadService.storeFile(file));
  }

  @RequestMapping(value = "/multiple/", method = RequestMethod.POST)
  @ApiOperation("복수 파일 업로드(스웨거에서 동작하지 않음)")
  @ResponseStatus(value = HttpStatus.OK)
  public List<SuccessResponse<String>> uploadMultipleFiles(@RequestPart MultipartFile[] files){
    return Arrays.asList(files)
        .stream()
        .map(file -> uploadFile(file))
        .collect(Collectors.toList());
  }
}
