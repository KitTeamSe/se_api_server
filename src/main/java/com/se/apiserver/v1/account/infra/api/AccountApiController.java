package com.se.apiserver.v1.account.infra.api;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.security.service.AccountDetailService;
import com.se.apiserver.v1.account.domain.usecase.*;
import com.se.apiserver.v1.account.infra.dto.*;
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
    private final AccountVerifyUseCase accountVerifyUseCase;
    private final AccountFindPasswordUseCase accountFindPasswordUseCase;
    private final AccountFindIdUseCase accountFindIdUseCase;

    private final AccountDetailService accountDetailService;


    // TODO 인증 서버로 이전, 유니크 중복 검사
    @PostMapping(path = "/signup")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "존재하지 않는 사용자"), @ApiResponse(code = 400, message = "비밀번호 불일치")})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "회원 가입")
    public SuccessResponse<Long> signUp(@RequestBody @Validated AccountCreateDto.Request request, HttpServletRequest httpServletRequest) {
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "회원가입에 성공했습니다.",
                accountCreateUseCase.signUp(request, httpServletRequest.getRemoteAddr()));
    }

    // TODO 인증 서버로 이전
    @PostMapping(path = "/signin")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "비밀번호 불일치")})
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "로그인")
    public SuccessResponse<AccountSignInDto.Response> signIn(@RequestBody @Validated AccountSignInDto.Request request) {
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 로그인 되었습니다",
                accountSignInUseCase.signIn(request.getId(), request.getPw()));
    }



    /// 아래 실제 유스케이스

    @GetMapping(path = "/account/email/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "이메일로 아이디 찾기")
    public SuccessResponse<AccountFindIdByEmailDto.Response> findAccountByEmail(
            @ApiParam(value = "사용자 이메일", example = "user@user.com") @PathVariable(value = "email") String email) {
        return new SuccessResponse(HttpStatus.OK.value(), "조회 성공", accountFindIdUseCase.readByEmail(email));
    }

    @PostMapping(path = "/account/verify/email/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "이메일로 인증 url 요청(email)")
    public SuccessResponse verifyEmailByEmail(@ApiParam(value = "사용자 이메일", example = "user") @PathVariable(value = "email") String email) {
        accountVerifyUseCase.sendVerifyRequestEmailByEmail(email);
        return new SuccessResponse(HttpStatus.OK.value(), "메일이 발송되었습니다.");
    }

    @PostMapping(path = "/account/verify/id/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "이메일로 인증 url 요청(id)")
    public SuccessResponse verifyEmailById(@ApiParam(value = "사용자 아이디", example = "user") @PathVariable(value = "id") String id) {
        accountVerifyUseCase.sendVerifyRequestEmailByAccountId(id);
        return new SuccessResponse(HttpStatus.OK.value(), "메일이 발송되었습니다.");
    }

    //TODO FE 요구사항에 따라서 수정
    @GetMapping(path = "/account/verify/{token}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "이메일 인증")
    public SuccessResponse verifyEmail(@ApiParam(value = "토큰", example = "token_value") @PathVariable(value = "token") String token) {
        accountVerifyUseCase.verify(token);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 인증되었습니다.");
    }

    @PostMapping(path = "/account/password")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "비밀번호 찾기")
    public SuccessResponse findPassword(@RequestBody @Validated AccountFindPasswordDto.Request request) {
        accountFindPasswordUseCase.findPassword(request);
        return new SuccessResponse(HttpStatus.OK.value(), "메일이 발송되었습니다.");
    }

    @PutMapping("/account")
    @PreAuthorize("hasAnyAuthority('ACCOUNT_ACCESS', 'ACCOUNT_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "회원 정보 수정")
    public SuccessResponse updateAccount(@RequestBody @Validated AccountUpdateDto.Request request) {
        accountUpdateUseCase.update(request);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/account")
    @PreAuthorize("hasAnyAuthority('ACCOUNT_ACCESS', 'ACCOUNT_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "회원 삭제")
    public SuccessResponse deleteAccount(@RequestBody @Validated AccountDeleteDto.Request request) {
        accountDeleteUseCase.delete(request);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다.");
    }

    @GetMapping("/account/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "회원 정보 조회")
    public SuccessResponse<AccountReadDto.Response> deleteAccount(@PathVariable(name = "id") String id) {
        accountReadUseCase.read(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다.");
    }

    //TODO 페이징 리퀘스트, 리스폰스 샘플 코드, 추후 삭제 요망
    @PostMapping(path = "/account")
    @PreAuthorize("hasAnyAuthority('ACCOUNT_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 목록 조회")
    public SuccessResponse<Page<Account>> readAllAccount(@RequestBody @Validated PageRequest pageRequest) {
        return new SuccessResponse(HttpStatus.OK.value(), "조회 성공", accountReadUseCase.readAll(pageRequest));
    }


    @PostMapping("/account/search")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "회원 검색")
    public SuccessResponse<Page<Account>> searchAccount(@RequestBody @Validated AccountReadDto.SearchRequest searchRequest) {
        return new SuccessResponse(HttpStatus.OK.value(), "조회 성공", accountReadUseCase.search(searchRequest));
    }


}
