package com.se.apiserver.v1.post.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.post.domain.usecase.PostCreateUseCase;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("게시글 관리")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostApiController {

    private final PostCreateUseCase postCreateUseCase;

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("게시글 생성")
    public SuccessResponse<PostReadDto.Response> create(@RequestBody @Validated PostCreateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다", postCreateUseCase.create(request));
    }

}
