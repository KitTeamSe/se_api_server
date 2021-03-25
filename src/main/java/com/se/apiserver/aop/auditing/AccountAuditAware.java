package com.se.apiserver.aop.auditing;

import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountAuditAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null)
            return null;
        return Optional.of(Long.parseLong(authentication.getName()));
    }
}
