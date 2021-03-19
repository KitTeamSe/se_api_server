package com.se.apiserver.http.api.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.usecase.account.AccountCreateUseCase;
import com.se.apiserver.domain.usecase.account.AccountDeleteUseCase;
import com.se.apiserver.domain.usecase.account.AccountReadUseCase;
import com.se.apiserver.domain.usecase.account.AccountUpdateUseCase;
import com.se.apiserver.http.dto.account.AccountCreateDto;
import com.se.apiserver.http.dto.account.AccountCreateDto.Request;
import com.se.apiserver.http.dto.account.AccountCreateDto.Response;
import com.se.apiserver.http.dto.account.AccountReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

  private final AccountCreateUseCase accountCreateUseCase;
  private final AccountReadUseCase accountReadUseCase;
  private final AccountUpdateUseCase accountUpdateUseCase;
  private final AccountDeleteUseCase accountDeleteUseCase;


  @PostMapping(path = "/api/v1/signup")
  public AccountCreateDto.Response createAccount(@RequestBody @Validated Request request){
    Long id = accountCreateUseCase.signUp(request);
    return new Response(id);
  }

  @GetMapping(path = "/api/v1/signin")
  public AccountReadDto.Response readAccount(@RequestParam Long id){
    Account account = accountReadUseCase.read(id);
    return new AccountReadDto.Response(account.getId(), account.getPassword());
  }
}
