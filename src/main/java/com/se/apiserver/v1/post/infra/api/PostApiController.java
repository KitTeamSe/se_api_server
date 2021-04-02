package com.se.apiserver.v1.post.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.post.domain.usecase.PostCreateUseCase;
import com.se.apiserver.v1.post.domain.usecase.PostDeleteUseCase;
import com.se.apiserver.v1.post.domain.usecase.PostReadUseCase;
import com.se.apiserver.v1.post.domain.usecase.PostUpdateUseCase;
import com.se.apiserver.v1.post.infra.dto.PostCreateDto;
import com.se.apiserver.v1.post.infra.dto.PostReadDto;
import com.se.apiserver.v1.post.infra.dto.PostUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "게시글 관리")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostApiController {

    private final PostCreateUseCase postCreateUseCase;
    private final PostUpdateUseCase postUpdateUseCase;
    private final PostReadUseCase postReadUseCase;
    private final PostDeleteUseCase postDeleteUseCase;

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("게시글 생성")
    public SuccessResponse<Long> create(@RequestBody @Validated PostCreateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다", postCreateUseCase.create(request));
    }

    @PutMapping("/post")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("게시글 수정")
    public SuccessResponse<Long> update(@RequestBody @Validated PostUpdateDto.Request request){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 수정되었습니다", postUpdateUseCase.update(request));
    }

    @DeleteMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("게시글 삭제")
    public SuccessResponse delete(@PathVariable(value = "id") Long postId){
        postDeleteUseCase.delete(postId);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
    }

    @GetMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("게시글 조회")
    public SuccessResponse<PostReadDto.Response> read(@PathVariable(value = "id") Long postId){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다", postReadUseCase.read(postId));
    }

    @GetMapping("/post/secret")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("게시글 조회")
    public SuccessResponse<PostReadDto.Response> readSecret(Long postId, String password){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
                postReadUseCase.readAnonymousSecretPost(postId,password));
    }

    @GetMapping("/post")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("게시글 조회")
    public SuccessResponse<PageImpl<PostReadDto.ListResponse>> readSecret(PageRequest pageRequest, Long boardId){
        return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
                postReadUseCase.readBoardPostList(pageRequest.of(),boardId));
    }
}
