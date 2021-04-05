package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto.Response;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PeriodReadService {

  private final PeriodJpaRepository periodJpaRepository;

  public PeriodReadDto.Response read(Long id){
    Period period = periodJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
    return PeriodReadDto.Response.fromEntity(period);
  }

  public PageImpl readAll(Pageable pageable){
    Page<Period> all = periodJpaRepository.findAll(pageable);
    List<Response> responseList = all
        .stream()
        .map(p -> PeriodReadDto.Response.fromEntity(p))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
