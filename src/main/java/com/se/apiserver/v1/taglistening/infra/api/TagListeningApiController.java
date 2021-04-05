package com.se.apiserver.v1.taglistening.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.taglistening.application.service.TagListeningCreateService;
import com.se.apiserver.v1.taglistening.application.service.TagListeningDeleteService;
import com.se.apiserver.v1.taglistening.application.service.TagListeningReadService;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningCreateDto;
import com.se.apiserver.v1.taglistening.application.dto.TagListeningReadDto;
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
@Api(tags = "수신 태그 관리")
public class TagListeningApiController {

    private final TagListeningCreateService tagListeningCreateService;
    private final TagListeningReadService tagListeningReadService;
    private final TagListeningDeleteService tagListeningDeleteService;

    @PostMapping("/tag-listen")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "수신 태그 등록")
    @PreAuthorize("hasAnyAuthority('TAG_ACCESS', 'TAG_MANAGE')")
    public SuccessResponse<Long> create(@RequestBody @Validated TagListeningCreateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "등록에 성공했습니다", tagListeningCreateService.create(request));
    }

    @DeleteMapping("/tag-listen/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "수신 태그 삭제")
    @PreAuthorize("hasAnyAuthority('TAG_ACCESS', 'TAG_MANAGE')")
    public SuccessResponse delete(@PathVariable(value = "id") Long id){
        tagListeningDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "삭제에 성공했습니다");
    }

    @GetMapping("/tag-listen/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "수신 태그 조회")
    @PreAuthorize("hasAnyAuthority('TAG_ACCESS', 'TAG_MANAGE')")
    public SuccessResponse<TagListeningReadDto.Response> read(@PathVariable(value = "id") Long id){
        return new SuccessResponse<>(HttpStatus.OK.value(), "조회에 성공했습니다", tagListeningReadService.read(id));
    }

    @GetMapping("/tag-listen/account/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "사용자 아이디로 수신 목록 조회")
    @PreAuthorize("hasAnyAuthority('TAG_ACCESS', 'TAG_MANAGE')")
    public SuccessResponse<List<TagListeningReadDto.Response>> readMy(@PathVariable(value = "id") Long accountId){
        return new SuccessResponse<>(HttpStatus.OK.value(), "조회에 성공했습니다", tagListeningReadService.readMy(accountId));
    }

    @GetMapping("/tag-listen")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "전체 수신 태그 조회(페이징)")
    @PreAuthorize("hasAuthority('TAG_MANAGE')")
    public SuccessResponse<PageImpl> readAll(@Validated PageRequest pageRequest){
        return new SuccessResponse<>(HttpStatus.OK.value(), "조회에 성공했습니다", tagListeningReadService.readAllByPaging(pageRequest.of()));
    }


}
