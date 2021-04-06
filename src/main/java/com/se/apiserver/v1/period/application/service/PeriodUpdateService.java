package com.se.apiserver.v1.period.application.service;


import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto;
import com.se.apiserver.v1.period.application.dto.PeriodUpdateDto;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeriodUpdateService {

  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public PeriodReadDto.Response update(PeriodUpdateDto.Request request){
    Period period = periodJpaRepository
        .findById(request.getPeriodId())
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));

    // 순서가 변경되었으면 중복되는 순서 있는지 검사
    if(request.getPeriodOrder() != null){
      checkPeriodOrderDuplicate(request);
      period.updatePeriodOrder(request.getPeriodOrder());
    }

    // 이름이 변경되었으면 중복되는 이름 있는지 검사
    if(request.getName() != null){
      checkNameDuplicate(request);
      period.updateName(request.getName());
    }

    if(request.getStartTime() != null){
      period.updateStartTime(request.getStartTime());
    }
    
    if(request.getEndTime() != null){
      period.updateEndTime(request.getEndTime());
    }

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      period.updateNote(note);
    }

    // 변경된 시간이 교차되는지 검사
    checkTimeCrossing(period.getStartTime(), period.getEndTime());

    periodJpaRepository.save(period);
    return PeriodReadDto.Response.fromEntity(period);
  }

  private void checkPeriodOrderDuplicate(PeriodUpdateDto.Request request){
    Optional<Period> optionalPeriod = periodJpaRepository.findByPeriodOrder(request.getPeriodOrder());

    if(optionalPeriod.isPresent())
      if(!optionalPeriod.get().getPeriodId().equals(request.getPeriodId()))
        throw new BusinessException(PeriodErrorCode.DUPLICATED_PERIOD_ORDER);
  }

  private void checkNameDuplicate(PeriodUpdateDto.Request request){
    Optional<Period> optionalPeriod = periodJpaRepository.findByName(request.getName());

    if(optionalPeriod.isPresent())
      if(!optionalPeriod.get().getPeriodId().equals(request.getPeriodId()))
        throw new BusinessException(PeriodErrorCode.DUPLICATED_PERIOD_NAME);
  }

  private void checkTimeCrossing(LocalTime startTime, LocalTime endTime){
    if(startTime.isAfter(endTime))
      throw new BusinessException(PeriodErrorCode.CROSSING_START_END_TIME);
  }
}
