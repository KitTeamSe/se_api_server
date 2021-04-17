package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.dto.DeploymentReadDto;
import com.se.apiserver.v1.deployment.application.dto.DeploymentReadDto.PeriodRequest;
import com.se.apiserver.v1.deployment.application.dto.DeploymentReadDto.Response;
import com.se.apiserver.v1.deployment.application.error.DeploymentErrorCode;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeploymentReadService {

  private final DeploymentJpaRepository deploymentJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;
  private final PeriodJpaRepository periodJpaRepository;

  public DeploymentReadDto.Response read(Long id){
    Deployment deployment = deploymentJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(DeploymentErrorCode.NO_SUCH_DEPLOYMENT));
    return DeploymentReadDto.Response.fromEntity(deployment);
  }

  public PageImpl readAllByTimeTableId(Pageable pageable, Long timeTableId){
    TimeTable timeTable = timeTableJpaRepository
        .findById(timeTableId)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    Page<Deployment> all = deploymentJpaRepository
        .findAllByTimeTable(pageable, timeTable);
    List<Response> responseList = all
        .stream()
        .map(d -> DeploymentReadDto.Response.fromEntity(d))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }

  public List<Response> readAllByPeriod(PeriodRequest periodRequest){
    TimeTable timeTable = timeTableJpaRepository
        .findById(periodRequest.getTimeTableId())
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    Period period = periodJpaRepository
        .findById(periodRequest.getPeriodId())
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));

    List<Deployment> all = deploymentJpaRepository
        .findAllByTimeTableAndDayOfWeek(timeTable, periodRequest.getDayOfWeek());

    all.removeIf((d) -> !d.getPeriodRange().contains(period));

    return all.stream()
        .map(d -> DeploymentReadDto.Response.fromEntity(d))
        .collect(Collectors.toList());
  }
}
