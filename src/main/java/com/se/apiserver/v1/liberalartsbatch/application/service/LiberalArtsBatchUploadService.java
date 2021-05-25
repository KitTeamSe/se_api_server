package com.se.apiserver.v1.liberalartsbatch.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.liberalartsbatch.application.error.LiberalArtsBatchUploadErrorCode;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

  @Transactional
  public void upload(Long timeTableId, MultipartFile file) {
    if(file.getSize() <= 0)
      throw new BusinessException(LiberalArtsBatchUploadErrorCode.INVALID_FILE_SIZE);

    TimeTable timetable = timeTableJpaRepository.findById(timeTableId).orElseThrow(() -> {
      throw new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE);
    });

    // TODO: 엑셀 file 읽어서 DB에 반영
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());

    if(!extension.equals("xlsx") && !extension.equals("xls"))
      throw new BusinessException(LiberalArtsBatchUploadErrorCode.INVALID_EXTENSION);

    Workbook workbook = getWorkbook(extension, file);
    Sheet worksheet = workbook.getSheetAt(0);

    for(int i = 5 ; i < worksheet.getPhysicalNumberOfRows() ; i++){
      Row row = worksheet.getRow(i);

      // 교과 정보 추출
      String curriculum = getCellValue(row, 5, "알 수 없는 교육과정");            // 교육과정
      SubjectType type = getSubjectType(getCellValue(row, 1, "교선"));          // 교과구분
      String code = getCellValue(row, 3, "UNKNOWN");                           // 교과목코드
      String name = getCellValue(row, 2, "알 수 없는 교과목명");                  // 교과목명
      int grade = getCellValue(row, 0, 0);                                     // 학년
      int semester = timetable.getSemester();                                                    // 학기
      int credit = getCellValue(row, 6, 0);                                    // 학점

      // 개설 교과 추출
      int teachingTimePerWeek = getCellValue(row, 10, 5);                      // 주간 수업 시간
      int divisions = getCellValue(row, 4, 1);                                 // 학점
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

  private SubjectType getSubjectType(String batchSubjectType){
    switch(batchSubjectType){
      case "MSC":
        return SubjectType.MSC;
      default:
        return SubjectType.LIBERAL_ARTS;
    }
  }

  private <T> T getCellValue(Row row, int cellNum, T defaultValue){
    try{
      if(defaultValue.getClass().equals(String.class))
        return (T) row.getCell(cellNum).getStringCellValue();

      if(defaultValue.getClass().equals(Integer.class))
        return (T) Integer.valueOf(row.getCell(cellNum).getStringCellValue());

      throw new BusinessException(LiberalArtsBatchUploadErrorCode.INVALID_EXCEL_DATA);
    }
    catch (Exception e){
      e.printStackTrace();
      return defaultValue;
    }
  }
}
