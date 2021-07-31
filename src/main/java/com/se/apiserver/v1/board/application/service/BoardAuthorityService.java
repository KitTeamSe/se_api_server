package com.se.apiserver.v1.board.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAuthorityService {

  private final BoardJpaRepository boardJpaRepository;
  private final AccountContextService accountContextService;

  public void validateAccessAuthority(String nameEng) {
    Board board = boardJpaRepository.findByNameEng(nameEng).orElseThrow(() -> new BusinessException(
        BoardErrorCode.NO_SUCH_BOARD));

    board.validateAccessAuthority(accountContextService.getContextAuthorities());
  }

  public void validateManageAuthority(String nameEng) {
    Board board = boardJpaRepository.findByNameEng(nameEng).orElseThrow(() -> new BusinessException(
        BoardErrorCode.NO_SUCH_BOARD));

    board.validateManageAuthority(accountContextService.getContextAuthorities());
  }

}