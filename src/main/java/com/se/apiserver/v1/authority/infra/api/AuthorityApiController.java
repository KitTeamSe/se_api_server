package com.se.apiserver.v1.authority.infra.api;

import com.se.apiserver.v1.authority.application.service.authority.AuthorityReadService;
import com.se.apiserver.v1.authority.application.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "권한 관리")
public class AuthorityApiController {
    private final AuthorityReadService authorityReadService;

    @GetMapping("/authority/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<AuthorityReadDto.Response> read(@PathVariable(value = "id") Long id){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityReadService.read(id));
    }

    @GetMapping("/authority")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 목록 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<PageImpl<AuthorityReadDto.Response>> readAll(@Validated PageRequest pageRequest){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityReadService.readAll(pageRequest.of()));
    }
}
