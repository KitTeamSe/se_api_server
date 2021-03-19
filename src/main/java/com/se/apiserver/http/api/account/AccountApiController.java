package com.se.apiserver.http.api.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.usecase.account.*;
import com.se.apiserver.http.dto.account.AccountCreateDto;
import com.se.apiserver.http.dto.account.AccountCreateDto.Response;
import com.se.apiserver.http.dto.account.AccountReadDto;
import com.se.apiserver.http.dto.account.AccountSignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountCreateUseCase accountCreateUseCase;
    private final AccountReadUseCase accountReadUseCase;
    private final AccountUpdateUseCase accountUpdateUseCase;
    private final AccountDeleteUseCase accountDeleteUseCase;
    private final AccountSignInUseCase accountSignInUseCase;


    // TODO 인증 서버로 이전
    @PostMapping(path = "/api/v1/signup")
    public AccountCreateDto.Response signUp(@RequestBody @Validated AccountCreateDto.Request request) {
        Long id = accountCreateUseCase.signUp(request);
        return new Response(id);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_ACCESS')")
    @GetMapping(path = "/api/v1/user/{id}")
    public AccountReadDto.Response readAccount(@PathVariable(value = "id") Long id) {
        Account account = accountReadUseCase.read(id);
        return new AccountReadDto.Response(account.getIdString(), account.getPassword());
    }

    // TODO 인증 서버로 이전
    @PostMapping(path = "/api/v1/signin")
    public AccountSignInDto.Response signIn(@RequestBody @Validated AccountSignInDto.Request request) {
        System.out.println(request.getId());
        System.out.println(request.getPw());
        String token = accountSignInUseCase.signIn(request.getId(), request.getPw());
        return new AccountSignInDto.Response(token);
    }

}
