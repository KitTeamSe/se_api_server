package com.se.apiserver.v1.multipartfile.infra.api;

import io.swagger.annotations.Api;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "multipart-file/download")
@Api(tags = "파일 다운로드")
public class MultipartFileDownloadController {

  @Autowired
  private MultipartFileDownloadService multipartFileDownloadService;

  @GetMapping("/{saveName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String saveName, HttpServletRequest request)
      throws UnsupportedEncodingException {
    return multipartFileDownloadService.downloadResource(saveName);
  }
}
