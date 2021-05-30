package com.se.apiserver.v1.account.application.service;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.account.application.error.AccountErrorCode;
import com.se.apiserver.v1.account.application.dto.AccountSignInDto;
import com.se.apiserver.v1.account.infra.repository.AccountJpaRepository;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupReadService;
import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadService;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.infra.security.provider.JwtTokenResolver;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountSignInService {

  private final JwtTokenResolver jwtTokenResolver;
  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;

  private final AuthorityGroupReadService authorityGroupReadService;
  private final AuthorityGroupAccountMappingReadService authorityGroupAccountMappingReadService;

  @Transactional
  public AccountSignInDto.Response signIn(String id, String password, String ip) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

    String token = signIn(account, password, ip);

    return new AccountSignInDto.Response(token);
  }

  @Transactional
  public AccountSignInDto.Response signInAsManager(String id, String password, String ip) {
    Account account = accountJpaRepository.findByIdString(id)
        .orElseThrow(() -> new BusinessException(AccountErrorCode.NO_SUCH_ACCOUNT));

    validateManagerAuthority(account);

    String token = signIn(account, password, ip);

    return new AccountSignInDto.Response(token);
  }

  private String signIn(Account account, String password, String ip){
    if (!passwordEncoder.matches(password, account.getPassword())) {
      throw new BusinessException(AccountErrorCode.PASSWORD_INCORRECT);
    }

    account.updateLastSignIp(ip);
    accountJpaRepository.save(account);

    return jwtTokenResolver.createToken(String.valueOf(account.getAccountId()));
  }

  private void validateManagerAuthority(Account account){
    // 기본적으로 그룹 타입이 SYSTEM인 경우에만 관리자로 인식함. (실제 가진 권한과 무관히게)
    // TODO : 다른 그룹을 관리자로 인식시키려면 로직 수정 필요.
    AuthorityGroup managerGroup = authorityGroupReadService
        .getAuthorityGroupsByType(AuthorityGroupType.SYSTEM).get(0);

    boolean isManager = authorityGroupAccountMappingReadService
        .isAccountMappedToGroup(account.getAccountId(), managerGroup);

    if(!isManager)
      throw new BusinessException(GlobalErrorCode.HANDLE_ACCESS_DENIED);
  }
}
