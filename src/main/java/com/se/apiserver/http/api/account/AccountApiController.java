package com.se.apiserver.http.api.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.usecase.account.*;
import com.se.apiserver.http.dto.account.AccountCreateDto;
import com.se.apiserver.http.dto.account.AccountCreateDto.Response;
import com.se.apiserver.http.dto.account.AccountReadDto;
import com.se.apiserver.http.dto.account.AccountSignInDto;
import com.se.apiserver.http.dto.common.PageRequest;
import com.se.apiserver.http.dto.common.SuccessResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "사용자 관리")
public class AccountApiController {

    private final AccountCreateUseCase accountCreateUseCase;
    private final AccountReadUseCase accountReadUseCase;
    private final AccountUpdateUseCase accountUpdateUseCase;
    private final AccountDeleteUseCase accountDeleteUseCase;
    private final AccountSignInUseCase accountSignInUseCase;


    // TODO 인증 서버로 이전, 유니크 중복 검사
    @PostMapping(path = "/signup")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "존재하지 않는 사용자"), @ApiResponse(code = 400, message = "비밀번호 불일치")})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "회원 가입")
    public SuccessResponse<Long> signUp(@RequestBody @Validated AccountCreateDto.Request request, HttpServletRequest httpServletRequest) {
        Long id = accountCreateUseCase.signUp(request, httpServletRequest);
        return new SuccessResponse<>(HttpStatus.CREATED.value(),"회원가입에 성공했습니다.", id);
    }

    // TODO 인증 서버로 이전
    @PostMapping(path = "/signin")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "비밀번호 불일치")})
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "로그인")
    public SuccessResponse<AccountSignInDto.Response> signIn(@RequestBody @Validated AccountSignInDto.Request request) {
        String token = accountSignInUseCase.signIn(request.getId(), request.getPw());
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 로그인 되었습니다", new AccountSignInDto.Response(token));
    }

    @GetMapping(path = "/account/{id}")
    @PreAuthorize("hasAuthority('ACCOUNT_ACCESS')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 조회")
    public SuccessResponse<AccountReadDto.Response> readAccount(@ApiParam(value = "사용자 아이디", example = "1") @PathVariable(value = "id") Long id) {
        Account account = accountReadUseCase.read(id);
        return new SuccessResponse<>(HttpStatus.OK.value(),"조회 성공",new AccountReadDto.Response(account.getIdString(), account.getPassword()));
    }

    @PostMapping(path = "/account")
    @PreAuthorize("hasAuthority('ACCOUNT_ACCESS')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 목록 조회")
    public SuccessResponse<Page<Account>> readAllAccount(@RequestBody @Validated PageRequest pageRequest) {
        return new SuccessResponse<>(HttpStatus.OK.value(),"조회 성공",accountReadUseCase.readAll(pageRequest));
    }
}
