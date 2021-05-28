package com.se.apiserver.v1.notice.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.notice.domain.service.*;
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
@Api(tags = "알림 관리")
public class NoticeApiController {

    private final NoticeReadService noticeReadUseCase;

    @GetMapping(path = "/notice")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 알림 조회")
    @PreAuthorize("hasAuthority('NOTICE_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest) {
        return new SuccessResponse(HttpStatus.OK.value(), "알림 목록 조회에 성공했습니다.", noticeReadUseCase.readAll(pageRequest.of()));
    }

    @GetMapping(path = "/notice/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "알림 아이디로 알림 조회")
    @PreAuthorize("hasAuthority('NOTICE_MANAGE')")
    public SuccessResponse readNotice(@PathVariable(value = "id")Long id) {
        return new SuccessResponse(HttpStatus.OK.value(), "알림 조회 성공", noticeReadUseCase.readById(id));
    }

}
