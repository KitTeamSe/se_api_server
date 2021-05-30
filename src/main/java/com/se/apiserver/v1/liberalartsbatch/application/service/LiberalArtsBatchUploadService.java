package com.se.apiserver.v1.liberalartsbatch.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.liberalartsbatch.application.dto.LiberalArtsBatchUploadDto;
import com.se.apiserver.v1.liberalartsbatch.application.dto.LiberalArtsBatchUploadDto.Response;
import com.se.apiserver.v1.liberalartsbatch.application.error.LiberalArtsBatchUploadErrorCode;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LiberalArtsBatchUploadService {

  private final TimeTableJpaRepository timeTableJpaRepository;
  private final LiberalArtsBatchParseService parseService;

  @Transactional
  public LiberalArtsBatchUploadDto.Response upload(Long timeTableId, MultipartFile file) {
    if(file.getSize() <= 0)
      throw new BusinessException(LiberalArtsBatchUploadErrorCode.INVALID_FILE_SIZE);

    TimeTable timeTable = timeTableJpaRepository.findById(timeTableId)
        .orElseThrow(() -> {throw new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE); });

    String extension = FilenameUtils.getExtension(file.getOriginalFilename());

    if(!extension.equals("xlsx") && !extension.equals("xls"))
      throw new BusinessException(LiberalArtsBatchUploadErrorCode.INVALID_EXTENSION);

    Workbook workbook = getWorkbook(extension, file);

    try{
      LiberalArtsBatchUploadDto.Response response = parseService.parse(timeTable, workbook);
      return response;
    }
    catch (Exception e){
      return LiberalArtsBatchUploadDto.Response.builder().newlyDeployed(0).build();
    }
  }

  private Workbook getWorkbook(String extension, MultipartFile file){
    Workbook workbook = null;
    try{
      if(extension.equals("xlsx"))
        workbook = new XSSFWorkbook(file.getInputStream());
      else
        workbook = new HSSFWorkbook(file.getInputStream());
      return workbook;
    }
    catch (IOException ie){
      throw new BusinessException(LiberalArtsBatchUploadErrorCode.FAILED_TO_GET_WORKBOOK);
    }
  }


}
