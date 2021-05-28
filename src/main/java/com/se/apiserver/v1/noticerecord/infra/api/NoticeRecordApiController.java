package com.se.apiserver.v1.noticerecord.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.noticerecord.domain.service.NoticeRecordDeleteService;
import com.se.apiserver.v1.noticerecord.domain.service.NoticeRecordReadService;
import com.se.apiserver.v1.noticerecord.infra.dto.NoticeRecordReadDto;
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
@Api(tags = "알림 내역")
public class NoticeRecordApiController {

    private final NoticeRecordReadService noticeRecordReadService;
    private final NoticeRecordDeleteService noticeRecordDeleteService;

    @GetMapping(path = "/noticeRecord")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 알림내역 조회")
    @PreAuthorize("hasAuthority('NOTICE_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest) {
        return new SuccessResponse(HttpStatus.OK.value(), "알림내역 목록 조회에 성공했습니다.", noticeRecordReadService.readAll(pageRequest.of()));
    }

    @GetMapping(path = "/noticeRecord/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "알림내역 아이디로 알림내역 조회")
    @PreAuthorize("hasAuthority('NOTICE_ACCESS')")
    public SuccessResponse<NoticeRecordReadDto.Response> readById(@PathVariable(value = "id") Long id) {
        return new SuccessResponse(HttpStatus.OK.value(), "알림내역 조회에 성공했습니다.", noticeRecordReadService.readById(id));
    }

    @GetMapping(path = "/noticeRecord/{accountId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 아이디로 알림내역 조회")
    @PreAuthorize("hasAuthority('NOTICE_ACCESS')")
    public SuccessResponse<NoticeRecordReadDto.Response> readByAccountId(@PathVariable(value = "accountId") Long accountId) {
        return new SuccessResponse(HttpStatus.OK.value(), "알림내역 조회에 성공했습니다.", noticeRecordReadService.readByAccountId(accountId));
    }

    @GetMapping(path = "/noticeRecord/{noticeId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "알림 아이디로 알림내역 조회")
    @PreAuthorize("hasAuthority('NOTICE_ACCESS')")
    public SuccessResponse<NoticeRecordReadDto.Response> readByNoticeId(@PathVariable(value = "noticeID") Long noticeId) {
        return new SuccessResponse(HttpStatus.OK.value(), "알림내역 조회에 성공했습니다.", noticeRecordReadService.readByNoticeId(noticeId));
    }

    @DeleteMapping(path = "/noticeRecord/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "알림내역 삭제")
    @PreAuthorize("hasAuthority('NOTICE_ACCESS')")
    public SuccessResponse delete(@PathVariable(value = "id") Long id) {
        noticeRecordDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "알림 내역 삭제에 성공했습니다.");
    }

}
