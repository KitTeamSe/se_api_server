package com.se.apiserver.v1.tag.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.tag.domain.usecase.tag.TagCreateUseCase;
import com.se.apiserver.v1.tag.domain.usecase.tag.TagDeleteUseCase;
import com.se.apiserver.v1.tag.domain.usecase.tag.TagReadUseCase;
import com.se.apiserver.v1.tag.domain.usecase.tag.TagUpdateUseCase;
import com.se.apiserver.v1.tag.infra.dto.tag.TagCreateDto;
import com.se.apiserver.v1.tag.infra.dto.tag.TagReadDto;
import com.se.apiserver.v1.tag.infra.dto.tag.TagUpdateDto;
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

    private final TagCreateUseCase tagCreateUseCase;
    private final TagUpdateUseCase tagUpdateUseCase;
    private final TagDeleteUseCase tagDeleteUseCase;
    private final TagReadUseCase tagReadUseCase;

    @PostMapping(path = "/tag")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "태그 등록")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<Long> create(@RequestBody @Validated TagCreateDto.Request request) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "태그 등록에 성공했습니다.", tagCreateUseCase.create(request));
    }

    @GetMapping(path = "/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "태그 아이디로 태그 조회")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<TagReadDto.Response> readById(@PathVariable(value = "id") Long id) {
        return new SuccessResponse(HttpStatus.OK.value(), "태그 조회에 성공했습니다.", tagReadUseCase.readById(id));
    }

    @GetMapping(path = "/tag")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 태그 조회")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest) {
        return new SuccessResponse(HttpStatus.OK.value(), "태그 목록 조회에 성공했습니다.", tagReadUseCase.readAll(pageRequest.of()));
    }


    @GetMapping(path = "/tag/match/{text}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "텍스트 포함하는 태그 조회")
    public SuccessResponse<List<TagReadDto.Response>> readMatchText(@PathVariable(value = "text") String text) {
        return new SuccessResponse(HttpStatus.OK.value(), "태그 조회에 성공했습니다.", tagReadUseCase.readMatchText(text));
    }

    @PutMapping(path = "/tag")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "태그 수정")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<Long> update(@RequestBody @Validated TagUpdateDto.Request request) {
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 수정되었습니다.", tagUpdateUseCase.update(request));
    }

    @DeleteMapping(path = "/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "태그 삭제")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse delete(@PathVariable(value = "text") Long id) {
        tagDeleteUseCase.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다.");
    }
}
