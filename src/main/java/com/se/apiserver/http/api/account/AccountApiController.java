package com.se.apiserver.http.api.account;

import com.se.apiserver.domain.entity.account.Account;
import com.se.apiserver.domain.usecase.account.*;
import com.se.apiserver.http.dto.account.AccountCreateDto;
import com.se.apiserver.http.dto.account.AccountFindPasswordDto;
import com.se.apiserver.http.dto.account.AccountReadDto.ReadAllResponse;
import com.se.apiserver.http.dto.account.AccountSignInDto;
import com.se.apiserver.http.dto.account.AccountFindIdByEmailDto;
import com.se.apiserver.http.dto.account.AccountFindIdByEmailDto.Response;
import com.se.apiserver.http.dto.common.PageRequest;
import com.se.apiserver.http.dto.common.SuccessResponse;
import com.se.apiserver.security.service.AccountDetailService;
import io.swagger.annotations.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

  private final AccountDetailService accountDetailService;


  // TODO 인증 서버로 이전, 유니크 중복 검사
  @PostMapping(path = "/signup")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "존재하지 않는 사용자"), @ApiResponse(code = 400, message = "비밀번호 불일치")})
  @ResponseStatus(value = HttpStatus.CREATED)
  @ApiOperation(value = "회원 가입")
  public SuccessResponse<Long> signUp(@RequestBody @Validated AccountCreateDto.Request request,
      HttpServletRequest httpServletRequest) {
    Long id = accountCreateUseCase.signUp(request, httpServletRequest);
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "회원가입에 성공했습니다.", id);
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


  //TODO 페이징 리퀘스트, 리스폰스 샘플 코드, 추후 삭제 요망
  @PostMapping(path = "/account")
  @PreAuthorize("hasAnyAuthority('ACCOUNT_ACCESS', 'ACCOUNT_MANAGE')")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "사용자 목록 조회")
  public SuccessResponse<Page<Account>> readAllAccount(@RequestBody @Validated PageRequest pageRequest) {
    Page<Account> accountsPage = accountReadUseCase.readAll(pageRequest);
    List<ReadAllResponse> dto = accountsPage.get()
        .map(a -> ReadAllResponse.create(a, accountDetailService.hasAuthority("ACCOUNT_MANAGE")))
        .collect(Collectors.toList());
    return new SuccessResponse(HttpStatus.OK.value(), "조회 성공",
        new PageImpl<>(dto, accountsPage.getPageable(), accountsPage.getTotalElements()));
  }

  /// 아래 실제 유스케이스

  @GetMapping(path = "/account/email/{email}")
  @PreAuthorize("hasAnyAuthority('ACCOUNT_ACCESS')")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "이메일로 아이디 찾기")
  public SuccessResponse<AccountFindIdByEmailDto.Response> findAccountByEmail(
      @ApiParam(value = "사용자 이메일", example = "user@user.com") @PathVariable(value = "email") String email) {
    Account account = accountReadUseCase.readByEmail(email);
    return new SuccessResponse(HttpStatus.OK.value(), "조회 성공", new Response(account.getIdString()));
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
  @ApiOperation(value = "이메일로 인증")
  public SuccessResponse verifyEmail(@ApiParam(value = "토큰", example = "token_value") @PathVariable(value = "token") String token) {
     accountVerifyUseCase.verify(token);
      return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 인증되었습니다");
  }

  @PostMapping(path = "/account/password")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "비밀번호 찾기")
  public SuccessResponse findPassword(@RequestBody @Validated AccountFindPasswordDto.Request request) {
    accountFindPasswordUseCase.findPassword(request);
    return new SuccessResponse(HttpStatus.OK.value(), "메일이 발송되었습니다.");
  }
}
