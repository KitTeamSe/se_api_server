package com.se.apiserver.v1.liberalartsbatchfile.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.liberalartsbatchfile.application.error.LiberalArtsBatchFileUploadErrorCode;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LiberalArtsBatchFileUploadService {

  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public void upload(Long timeTableId, MultipartFile file) {
    if(file.getSize() <= 0)
      throw new BusinessException(LiberalArtsBatchFileUploadErrorCode.INVALID_FILE_SIZE);

    if(!timeTableJpaRepository.findById(timeTableId).isPresent())
      throw new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE);

    // TODO: 엑셀 file 읽어서 DB에 반영
  }
}
