package com.se.apiserver.v1.authority.infra.api;

import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupCreateService;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupDeleteService;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupReadService;
import com.se.apiserver.v1.authority.application.service.authoritygroup.AuthorityGroupUpdateService;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupCreateDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupReadDto;
import com.se.apiserver.v1.authority.application.dto.authoritygroup.AuthorityGroupUpdateDto;
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
@Api(tags = "권한 그룹 관리")
public class AuthorityGroupApiController {
    private final AuthorityGroupReadService authorityGroupReadService;
    private final AuthorityGroupUpdateService authorityGroupUpdateService;
    private final AuthorityGroupDeleteService authorityGroupDeleteService;
    private final AuthorityGroupCreateService authorityGroupCreateService;

    @GetMapping("/authority-group/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<AuthorityGroupReadDto.Response> read(@PathVariable(value = "id") Long id){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityGroupReadService.read(id));
    }

    @GetMapping("/authority-group")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹 목록 조회")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", authorityGroupReadService.readAll(pageRequest.of()));
    }

    @PostMapping("/authority-group")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "권한 그룹 등록")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<AuthorityGroupReadDto.Response> create(@RequestBody @Validated AuthorityGroupCreateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다", authorityGroupCreateService.create(request));
    }

    @PutMapping("/authority-group")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹 수정")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse<AuthorityGroupReadDto.Response> create(@RequestBody @Validated AuthorityGroupUpdateDto.Request request){
        return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 수정되었습니다", authorityGroupUpdateService.update(request));
    }

    @PutMapping("/authority-group/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "권한 그룹 삭제")
    @PreAuthorize("hasAuthority('AUTHORITY_MANAGE')")
    public SuccessResponse create(@PathVariable(value = "id") Long id){
        authorityGroupDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
    }
}
