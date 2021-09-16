package com.se.apiserver.v1.attach.application.service;

import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto.Response;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class AttachCreateService {

  private final MultipartFileUploadService multipartFileUploadService;
  private final AttachJpaRepository attachJpaRepository;

  public AttachCreateService(
      MultipartFileUploadService multipartFileUploadService,
      AttachJpaRepository attachJpaRepository) {
    this.multipartFileUploadService = multipartFileUploadService;
    this.attachJpaRepository = attachJpaRepository;
  }

  @Transactional
  public List<AttachReadDto.Response> create(MultipartFile... files) {
    List<Attach> attaches = getAttaches(files);
    return attachJpaRepository.saveAll(attaches)
        .stream()
        .map(Response::fromEntity)
        .collect(Collectors.toList());
  }

  private List<Attach> getAttaches(MultipartFile... files) {
    List<MultipartFileUploadDto> multipartFileUploadDtoList = upload(files);
    List<Attach> attaches = new ArrayList<>();

    for (MultipartFileUploadDto multipartFileUploadDto : multipartFileUploadDtoList) {
      Attach attach
          = new Attach(multipartFileUploadDto.getDownloadUrl()
          , multipartFileUploadDto.getOriginalName(), multipartFileUploadDto.getFileSize());
      attaches.add(attach);
    }

    return attaches;
  }

  private List<MultipartFileUploadDto> upload(MultipartFile... files) {
     return multipartFileUploadService.upload(files);
  }
}
