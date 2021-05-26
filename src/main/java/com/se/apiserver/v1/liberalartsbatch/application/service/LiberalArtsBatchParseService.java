package com.se.apiserver.v1.liberalartsbatch.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.application.service.DeploymentCreateService;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomQueryRepository;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.liberalartsbatch.application.error.LiberalArtsBatchUploadErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LiberalArtsBatchParseService {

  private final int START_ROW = 4;

  private final int COL_SUBJECT_GRADE = 0;
  private final int COL_SUBJECT_TYPE = 1;
  private final int COL_SUBJECT_NAME = 2;
  private final int COL_SUBJECT_CODE = 3;
  private final int COL_DIVISION = 4;
  private final int COL_SUBJECT_CURRICULUM = 5;
  private final int COL_SUBJECT_CREDIT = 6;
  private final int COL_TEACHER_TYPE = 7;
  private final int COL_TEACHER_NAME = 8;
  private final int COL_DAY_OF_WEEK = 9;
  private final int COL_PERIOD = 10;
  private final int COL_LECTURE_ROOM = 11;
  private final int COL_LECTURE_ROOM_CAPACITY = 12;
  private final int COL_NOTE = 13;

  private final AccountContextService accountContextService;
  private final SubjectJpaRepository subjectJpaRepository;
  private final OpenSubjectJpaRepository openSubjectJpaRepository;
  private final TeacherJpaRepository teacherJpaRepository;
  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;
  private final LectureRoomQueryRepository lectureRoomQueryRepository;
  private final UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;
  private final PeriodJpaRepository periodJpaRepository;
  private final DeploymentCreateService deploymentCreateService;

  public void parse(TimeTable timeTable, Workbook workbook){
    // TODO: 엑셀 file 읽어서 DB에 반영;
    Sheet worksheet = workbook.getSheetAt(0);

    String note = createAutoCreatedNote();

    for(int i = START_ROW ; i < worksheet.getPhysicalNumberOfRows() ; i++){
      Row row = worksheet.getRow(i);

      // 교과 정보 생성
      Subject subject = parseSubject(row, timeTable.getSemester(), note);

      // 시작-종료 교시 생성
      PeriodRange periodRange = parsePeriodRange(row);
      if(periodRange == null)
        continue;

      // 개설 교과 생성
      OpenSubject openSubject = parseOpenSubject(timeTable, subject, note);

      // 분반 생성?
      Division division = parseDivision(row, openSubject, note);
      if(division == null)
        continue;

      // 교원 생성
      Teacher teacher = parseTeacher(row, note);

      // 참여 교원 생성
      ParticipatedTeacher participatedTeacher = parseParticipatedTeacher(timeTable, teacher, note);

      // 강의실 생성
      LectureRoom lectureRoom = parseLectureRoom(row, note);

      // 사용 가능 강의실 생성
      UsableLectureRoom usableLectureRoom = parseUsableLectureRoom(timeTable, lectureRoom, note);

      // 요일 추출
      DayOfWeek dayOfWeek = parseDayOfWeek(row);
      
      // 배치 생성
      DeploymentCreateDto.Resposne response = deploymentCreateService.saveDeployment(timeTable, division, 
          usableLectureRoom, participatedTeacher, periodRange, dayOfWeek, true, note);
    }
  }

  private Subject parseSubject(Row row, int semester, String note){
    String code = getCellValue(row, COL_SUBJECT_CODE, "UNKNOWN");                                  // 교과목코드
    Optional<Subject> subjectOptional = subjectJpaRepository.findByCode(code);
    if(subjectOptional.isPresent())
      return subjectOptional.get();

    String curriculum = getCellValue(row, COL_SUBJECT_CURRICULUM, "알 수 없는 교육과정");               // 교육과정
    SubjectType subjectType = getSubjectType(getCellValue(row, COL_SUBJECT_TYPE, "교선"));              // 교과구분
    String subjectName = getCellValue(row, COL_SUBJECT_NAME, "알 수 없는 교과목명");                    // 교과목명
    int grade = getCellValue(row, COL_SUBJECT_GRADE, 0);                                                // 학년
    int credit = getCellValue(row, COL_SUBJECT_CREDIT, 0);                                              // 학점
    return new Subject(curriculum, subjectType, code, subjectName, grade, semester, credit, true, note);
  }

  private PeriodRange parsePeriodRange(Row row){
    String periodRangeRawStr = getCellValue(row, COL_PERIOD, "");
    if(periodRangeRawStr.equals(""))
      return null;

    Period startPeriod, endPeriod;
    try{
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
      return new PeriodRange(startPeriod, endPeriod);
    }
    catch (Exception e){
      // Unknown period parsed! Log it!
      return null;
    }
  }

  private LectureRoom parseLectureRoom(Row row, String note){
    String buildingRawStr = getCellValue(row, COL_LECTURE_ROOM, "XX000");
    if(buildingRawStr.isEmpty())
      buildingRawStr = "XX000";

    String building;
    String roomNumber;
    try{
      int splitPoint = 0;
      for(int j = 0  ; j < buildingRawStr.length() ; j++){
        if(Character.isDigit(buildingRawStr.charAt(j))){
          splitPoint = j;
          break;
        }
      }
      building = buildingRawStr.substring(0, splitPoint);
      roomNumber = buildingRawStr.substring(splitPoint);
    }
    catch (Exception e){
      building = "XX";
      roomNumber = "000";
    }

    Optional<LectureRoom> optionalLectureRoom = lectureRoomQueryRepository.findByRoomNumberWithBuilding(building, roomNumber);
    if(optionalLectureRoom.isPresent())
      return optionalLectureRoom.get();

    int capacity = getCellValue(row, COL_LECTURE_ROOM_CAPACITY, 100);

    return new LectureRoom(building, roomNumber, capacity, true, note);
  }

  private UsableLectureRoom parseUsableLectureRoom(TimeTable timeTable, LectureRoom lectureRoom, String note){
    if(lectureRoom.getLectureRoomId() != null){
      Optional<UsableLectureRoom> optionalUsableLectureRoom = usableLectureRoomJpaRepository.findByTimeTableAndLectureRoom(timeTable, lectureRoom);
      if(optionalUsableLectureRoom.isPresent())
        return optionalUsableLectureRoom.get();
    }

    return new UsableLectureRoom(timeTable, lectureRoom, true, note);
  }

  private OpenSubject parseOpenSubject(TimeTable timeTable, Subject subject, String note){
    if(subject.getSubjectId() != null){
      Optional<OpenSubject> optionalOpenSubject = openSubjectJpaRepository.findByTimeTableAndSubject(timeTable, subject);
      if(optionalOpenSubject.isPresent())
        return optionalOpenSubject.get();
    }

    // TODO: 주간 수업 시간 엑셀에서 파싱해서 계산해야함.
    int teachingTimePerWeek = subject.getCredit();                                                  // 주간 수업 시간 (일단 학점과 동일)
    return new OpenSubject(timeTable, subject, 0, teachingTimePerWeek, true, note);
  }

  private Division parseDivision(Row row, OpenSubject openSubject, String note){
    int divisionNumber = getCellValue(row, COL_DIVISION, 0);
    if(divisionNumber == 0)
      return null;

    if(openSubject.getDivisions().size() < divisionNumber){
      return new Division(openSubject, true, note);
    }
    return openSubject.getDivisions().get(divisionNumber - 1);
  }

  private Teacher parseTeacher(Row row, String note){
    String teacherName = getCellValue(row, COL_TEACHER_NAME, "알 수 없는 성명");
    TeacherType teacherType = TeacherType.AUTO_CREATED;

    Optional<Teacher> optionalTeacher = teacherJpaRepository.findByNameAndType(teacherName, teacherType);
    if(optionalTeacher.isPresent())
      return optionalTeacher.get();

    String teacherDepartment = "알 수 없는 부서";
    return new Teacher(teacherName, teacherType, teacherDepartment, true, note);
  }

  private DayOfWeek parseDayOfWeek(Row row){
    String dayOfWeekRawStr = getCellValue(row, COL_DAY_OF_WEEK, "");
    if(dayOfWeekRawStr.equals(""))
      return null;
    return DayOfWeek.fromShortKorean(dayOfWeekRawStr);
  }

  private ParticipatedTeacher parseParticipatedTeacher(TimeTable timeTable, Teacher teacher, String note){
    if(teacher.getTeacherId() != null){
      Optional<ParticipatedTeacher> optionalParticipatedTeacher = participatedTeacherJpaRepository.findByTimeTableAndTeacher(timeTable, teacher);
      if(optionalParticipatedTeacher.isPresent())
        return optionalParticipatedTeacher.get();
    }

    return new ParticipatedTeacher(timeTable, teacher, true, note);
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

  private String createAutoCreatedNote(){
    StringBuilder stringBuilder = new StringBuilder("Auto created at ");
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    Account creator = accountContextService.getContextAccount();
    String nickname = "알 수 없음";
    if(creator != null)
      nickname = creator.getNickname();
    return stringBuilder.append(date).append(" by ").append(nickname).toString();
  }
}
