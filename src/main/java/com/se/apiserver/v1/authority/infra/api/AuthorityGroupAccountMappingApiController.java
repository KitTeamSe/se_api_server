package com.se.apiserver.v1.authority.infra.api;

import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateService;
import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingDeleteService;
import com.se.apiserver.v1.authority.application.service.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadService;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingCreateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroupaccountmapping.AuthorityGroupAccountMappingReadDto;
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
@Api(tags = "사용자-권한그룹 매핑 관리")
public class AuthorityGroupAccountMappingApiController {

    private final AuthorityGroupAccountMappingReadService authorityGroupAccountMappingReadService;
    private final AuthorityGroupAccountMappingCreateService authorityGroupAccountMappingCreateService;
    private final AuthorityGroupAccountMappingDeleteService authorityGroupAccountMappingDeleteService;

    @GetMapping("/authority-group-account/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹, 사용자 매핑 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<AuthorityGroupAccountMappingReadDto.Response> read(@PathVariable(value = "id") Long id){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityGroupAccountMappingReadService.read(id));
    }

    @GetMapping("/authority-group-account")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹, 사용자 매핑 목록 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityGroupAccountMappingReadService.readAll(pageRequest.of()));
    }

    @PostMapping("/authority-group-account")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "권한 그룹, 사용자 매핑 등록")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<Long> create(
            @RequestBody @Validated AuthorityGroupAccountMappingCreateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 생성되었습니다", authorityGroupAccountMappingCreateService.create(request));
    }

    @DeleteMapping("/authority-group-account/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹, 사용자 매핑 삭제")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse delete(@PathVariable(value = "id") Long id){
        authorityGroupAccountMappingDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
    }


}
