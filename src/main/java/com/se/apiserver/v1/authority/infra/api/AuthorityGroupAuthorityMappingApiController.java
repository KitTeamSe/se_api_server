package com.se.apiserver.v1.authority.infra.api;

import com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateService;
import com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingDeleteService;
import com.se.apiserver.v1.authority.application.service.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadService;
import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingCreateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping.AuthorityGroupAuthorityMappingReadDto;
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
@Api(tags = "권한-권한그룹 매핑 관리")
public class AuthorityGroupAuthorityMappingApiController {

    private final AuthorityGroupAuthorityMappingReadService authorityGroupAuthorityMappingReadService;
    private final AuthorityGroupAuthorityMappingDeleteService authorityGroupAuthorityMappingDeleteService;
    private final AuthorityGroupAuthorityMappingCreateService authorityGroupAuthorityMappingCreateService;

    @GetMapping("/authority-group-authority/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹, 권한 매핑 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<AuthorityGroupAuthorityMappingReadDto.Response> read(@PathVariable(value = "id") Long id){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityGroupAuthorityMappingReadService.read(id));
    }

    @GetMapping("/authority-group-authority")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹, 권한 매핑 목록 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityGroupAuthorityMappingReadService.readAll(pageRequest.of()));
    }

    @PostMapping("/authority-group-authority")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "권한 그룹, 권한 매핑 등록")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<Long> create(
            @RequestBody @Validated AuthorityGroupAuthorityMappingCreateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 생성되었습니다", authorityGroupAuthorityMappingCreateService.create(request));
    }

    @DeleteMapping("/authority-group-authority/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹, 권한 매핑 삭제")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse delete(@PathVariable(value = "id") Long id){
        authorityGroupAuthorityMappingDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
    }
}
