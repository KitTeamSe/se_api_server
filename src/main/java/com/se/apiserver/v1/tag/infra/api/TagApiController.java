package com.se.apiserver.v1.tag.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.tag.application.service.TagCreateService;
import com.se.apiserver.v1.tag.application.service.TagDeleteService;
import com.se.apiserver.v1.tag.application.service.TagReadService;
import com.se.apiserver.v1.tag.application.service.TagUpdateService;
import com.se.apiserver.v1.tag.application.dto.TagCreateDto;
import com.se.apiserver.v1.tag.application.dto.TagReadDto;
import com.se.apiserver.v1.tag.application.dto.TagUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "태그 관리")
public class TagApiController {

    private final TagCreateService tagCreateService;
    private final TagUpdateService tagUpdateService;
    private final TagDeleteService tagDeleteService;
    private final TagReadService tagReadService;

    @PostMapping(path = "/tag")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "태그 등록")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<Long> create(@RequestBody @Validated TagCreateDto.Request request) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "태그 등록에 성공했습니다.", tagCreateService.create(request));
    }

    @GetMapping(path = "/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "태그 아이디로 태그 조회")
    @PreAuthorize("hasAnyAuthority('TAG_ACCESS', 'TAG_MANAGE')")
    public SuccessResponse<TagReadDto.Response> readById(@PathVariable(value = "id") Long id) {
        return new SuccessResponse(HttpStatus.OK.value(), "태그 조회에 성공했습니다.", tagReadService.readById(id));
    }

    @GetMapping(path = "/tag")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 태그 조회")
    @PreAuthorize("hasAnyAuthority('TAG_ACCESS', 'TAG_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest) {
        return new SuccessResponse(HttpStatus.OK.value(), "태그 목록 조회에 성공했습니다.", tagReadService.readAll(pageRequest.of()));
    }


    @GetMapping(path = "/tag/match/{text}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "텍스트 포함하는 태그 조회")
    public SuccessResponse<List<TagReadDto.Response>> readMatchText(@PathVariable(value = "text") String text) {
        return new SuccessResponse(HttpStatus.OK.value(), "태그 조회에 성공했습니다.", tagReadService.readMatchText(text));
    }

    @PutMapping(path = "/tag")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "태그 수정")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<Long> update(@RequestBody @Validated TagUpdateDto.Request request) {
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 수정되었습니다.", tagUpdateService.update(request));
    }

    @DeleteMapping(path = "/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "태그 삭제")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse delete(@PathVariable(value = "id") Long id) {
        tagDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다.");
    }
}
