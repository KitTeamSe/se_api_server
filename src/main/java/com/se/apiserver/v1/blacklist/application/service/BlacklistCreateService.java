package com.se.apiserver.v1.blacklist.application.service;

import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.blacklist.domain.entity.Blacklist;
import com.se.apiserver.v1.blacklist.application.error.BlacklistErrorCode;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.infra.repository.BlacklistJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.exception.SeException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BlacklistCreateService {

  private final BlacklistJpaRepository blacklistJpaRepository;
  private final AccountJpaRepository accountJpaRepository;

  @Value("${spring.blacklist.release.default-plus-day}")
  private Integer DEFAULT_PLUS_DAY;

  public BlacklistCreateService(
      BlacklistJpaRepository blacklistJpaRepository,
      AccountJpaRepository accountJpaRepository) {
    this.blacklistJpaRepository = blacklistJpaRepository;
    this.accountJpaRepository = accountJpaRepository;
  }

  @Transactional
  public Long create(BlacklistCreateDto.Request request) {

    isValidRequest(request);

    String ip = request.getIp();
    Account account = null;
    if (request.getAccountId() != null) {
      account = accountJpaRepository.findById(request.getAccountId())
          .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));
      ip = null;
    }

    Blacklist blacklist = new Blacklist(
        request.getIp(),
        account,
        request.getReason(),
        request.getReleaseDate() == null ? LocalDateTime.now().plusDays(DEFAULT_PLUS_DAY)
            : request.getReleaseDate()
    );

    blacklistJpaRepository.save(blacklist);
    return blacklist.getBlacklistId();
  }

  private void isValidRequest(BlacklistCreateDto.Request request) {
    if (request.getIp() == null && request.getAccountId() == null) {
      throw new BusinessException(BlacklistErrorCode.REQUIRED_AT_LEAST);
    }
  }

}
