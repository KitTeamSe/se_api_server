package com.se.apiserver.v1.blacklist.infra.api;

import com.se.apiserver.v1.blacklist.application.service.BlacklistCreateService;
import com.se.apiserver.v1.blacklist.application.service.BlacklistDeleteService;
import com.se.apiserver.v1.blacklist.application.service.BlacklistReadService;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistCreateDto;
import com.se.apiserver.v1.blacklist.application.dto.BlacklistReadDto;
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
@RequestMapping("/api/v1")
@Api(tags = "블랙리스트 관리")
@RequiredArgsConstructor
public class BlacklistApiController {

    private final BlacklistReadService blacklistReadService;
    private final BlacklistCreateService blacklistCreateService;
    private final BlacklistDeleteService blacklistDeleteService;

    @PreAuthorize("hasAuthority('ACCESS_MANAGE')")
    @GetMapping("/blacklist/{id}")
    @ApiOperation("블랙리스트 조회")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<BlacklistReadDto.Response> read(@PathVariable(value = "id") Long id){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", blacklistReadService.read(id));
    }

    @PreAuthorize("hasAuthority('ACCESS_MANAGE')")
    @GetMapping("/blacklist")
    @ApiOperation("블랙리스트 전체 조회")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다.", blacklistReadService.readAll(pageRequest.of()));
    }

    @PreAuthorize("hasAuthority('ACCESS_MANAGE')")
    @PostMapping("/blacklist")
    @ApiOperation("블랙리스트 등록")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<Long> create(@RequestBody @Validated BlacklistCreateDto.Request request){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 등록되었습니다.", blacklistCreateService.create(request));
    }

    @PreAuthorize("hasAuthority('ACCESS_MANAGE')")
    @PostMapping("/blacklist/{id}")
    @ApiOperation("블랙리스트 삭제")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse delete(@PathVariable(value = "id") Long id){
        blacklistDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다.");
    }


}
