package com.se.apiserver.v1.liberalartsbatch.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.liberalartsbatch.application.error.LiberalArtsBatchUploadErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import io.swagger.models.auth.In;
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
  private final PeriodJpaRepository periodJpaRepository;
  private final DeploymentJpaRepository deploymentJpaRepository;

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

    for(int i = 4 ; i < worksheet.getPhysicalNumberOfRows() ; i++){
      Row row = worksheet.getRow(i);

      // 교과 정보 생성
      String curriculum = getCellValue(row, 5, "알 수 없는 교육과정");                // 교육과정
      SubjectType subjectType = getSubjectType(getCellValue(row, 1, "교선"));         // 교과구분
      String code = getCellValue(row, 3, "UNKNOWN");                                  // 교과목코드
      String subjectName = getCellValue(row, 2, "알 수 없는 교과목명");               // 교과목명
      int grade = getCellValue(row, 0, 0);                                            // 학년
      int semester = timetable.getSemester();                                                               // 학기
      int credit = getCellValue(row, 6, 0);                                           // 학점

      Subject subject = new Subject(curriculum, subjectType, code, subjectName, grade, semester, credit, true, "autocreated by ~~~");

      // 시작-종료 교시 추출
      String periodRangeRawStr = getCellValue(row, 10, "");
      if(periodRangeRawStr.equals(""))
        continue;

      Period startPeriod, endPeriod;
      if(periodRangeRawStr.length() < 2){
        startPeriod = periodJpaRepository.findByName(String.valueOf(periodRangeRawStr.charAt(0)))
            .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
        endPeriod = startPeriod;
      }
      else{
        startPeriod = periodJpaRepository.findByName(String.valueOf(periodRangeRawStr.charAt(0)))
            .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
        endPeriod = periodJpaRepository.findByName(String.valueOf(periodRangeRawStr.charAt(periodRangeRawStr.length() - 1)))
            .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
      }

      // 개설 교과 생성
      int teachingTimePerWeek = credit;                                                                      // 주간 수업 시간 (일단 학점과 동일)
//      int numberOfDivisions = getCellValue(row, 4, 1);                                 // 분반 수

      OpenSubject openSubject = new OpenSubject(timetable, subject, null, teachingTimePerWeek, true, "autocreated by ~~~");

      // 분반 생성?
      Division division = new Division(openSubject);

      // 교원 생성
      String teacherName = getCellValue(row, 8, "알 수 없는 성명");
      TeacherType teacherType = TeacherType.ETC;
      String teacherDepartment = "알 수 없는 부서";
      Teacher teacher = new Teacher(teacherName, teacherType, teacherDepartment, true, "autocreated by ~~~");

      // 참여 교원 생성
      ParticipatedTeacher participatedTeacher = new ParticipatedTeacher(timetable, teacher, true, "autocreated by ~~~");

      // 강의실 생성
      String buildingRawStr = getCellValue(row, 11, "XX000");
      if(buildingRawStr.isEmpty())
        buildingRawStr = "XX000";

      String building;
      int roomNumber;
      try{
        int splitPoint = 0;
        for(int j = 0  ; j < buildingRawStr.length() ; j++){
          if(Character.isDigit(buildingRawStr.charAt(j))){
            splitPoint = j;
            break;
          }
        }
        building = buildingRawStr.substring(0, splitPoint);
        roomNumber = Integer.parseInt(buildingRawStr.substring(splitPoint));
      }
      catch (Exception e){
        building = "XX";
        roomNumber = 000;
      }

      int capacity = getCellValue(row, 12, 100);

      LectureRoom lectureRoom = new LectureRoom(building, roomNumber, capacity, true, "autocreated by ~~~");

      // 사용 가능 강의실 생성
      UsableLectureRoom usableLectureRoom = new UsableLectureRoom(timetable, lectureRoom, true, "autocreated by ~~~");

      // 배치 생성
      String dayOfWeekRawStr = getCellValue(row, 9, "");
      if(dayOfWeekRawStr.equals(""))
        continue;
      DayOfWeek dayOfWeek = DayOfWeek.fromShortKorean(dayOfWeekRawStr);

      PeriodRange periodRange = new PeriodRange(startPeriod, endPeriod);
      Deployment deployment = new Deployment(timetable, division, usableLectureRoom, participatedTeacher, dayOfWeek, periodRange, true, "autocreated by ~~~");

      deploymentJpaRepository.save(deployment);
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
      if(defaultValue.getClass().equals(String.class)){
        try{
          String value = row.getCell(cellNum).getStringCellValue();
          return value.isEmpty() || value.isBlank() ? defaultValue : (T) value;
        }
        catch (Exception e) {
          int value = Integer.valueOf((int)row.getCell(cellNum).getNumericCellValue());
          return (T) String.valueOf(value);
        }
      }

      if(defaultValue.getClass().equals(Integer.class))
        return (T) (Integer.valueOf((int)row.getCell(cellNum).getNumericCellValue()));

      throw new BusinessException(LiberalArtsBatchUploadErrorCode.INVALID_EXCEL_DATA);
    }
    catch (Exception e){
      e.printStackTrace();
      return defaultValue;
    }
  }



}
