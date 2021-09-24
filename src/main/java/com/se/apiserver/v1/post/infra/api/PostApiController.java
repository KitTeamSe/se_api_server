package com.se.apiserver.v1.post.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.post.application.dto.PostAccessCheckDto;
import com.se.apiserver.v1.post.application.dto.PostCreateDto;
import com.se.apiserver.v1.post.application.dto.PostDeleteDto;
import com.se.apiserver.v1.post.application.dto.PostReadDto;
import com.se.apiserver.v1.post.application.dto.PostReadDto.PostSearchRequest;
import com.se.apiserver.v1.post.application.dto.PostUpdateDto;
import com.se.apiserver.v1.post.application.service.PostCreateService;
import com.se.apiserver.v1.post.application.service.PostDeleteService;
import com.se.apiserver.v1.post.application.service.PostReadService;
import com.se.apiserver.v1.post.application.service.PostUpdateService;
import com.se.apiserver.v1.post.domain.entity.PostIsNotice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "게시글 관리")
@RequestMapping("/api/v1")
public class PostApiController {

  private final PostCreateService postCreateService;
  private final PostUpdateService postUpdateService;
  private final PostReadService postReadService;
  private final PostDeleteService postDeleteService;

  public PostApiController(
      PostCreateService postCreateService,
      PostUpdateService postUpdateService,
      PostReadService postReadService,
      PostDeleteService postDeleteService) {
    this.postCreateService = postCreateService;
    this.postUpdateService = postUpdateService;
    this.postReadService = postReadService;
    this.postDeleteService = postDeleteService;
  }

  @PostMapping(value = "/post")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "게시글 생성")
  public SuccessResponse<Long> create(@RequestBody @Validated PostCreateDto.Request request) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다",
        postCreateService.create(request));
  }

  @PutMapping("/post")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 수정")
  public SuccessResponse<Long> update(
      @RequestBody @Validated PostUpdateDto.Request request) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 수정되었습니다",
        postUpdateService.update(request));
  }

  @DeleteMapping("/post/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long postId) {
    postDeleteService.delete(postId);
    return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
  }


  @PostMapping("/post/anonymous/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("익명 게시글 삭제")
  public SuccessResponse delete(
      @RequestBody @Validated PostDeleteDto.AnonymousPostDeleteRequest request) {
    postDeleteService.delete(request);
    return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
  }

  @GetMapping("/post/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 조회")
  public SuccessResponse<PostReadDto.Response> read(@PathVariable(value = "id") Long postId) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
        postReadService.read(postId));
  }

  @GetMapping("/post/secret")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("비밀 게시글 조회")
  public SuccessResponse<PostReadDto.Response> readSecret(Long postId, String password) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
        postReadService.readAnonymousSecretPost(postId, password));
  }

  @GetMapping("/post")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시판에 따른 게시글 목록 조회")
  public SuccessResponse<PostReadDto.PostListResponse> readBoardPostList(PageRequest pageRequest,
      String boardNameEng, PostIsNotice isNotice) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
        postReadService.readBoardPostList(pageRequest.of(), boardNameEng, isNotice));
  }

  @PostMapping("/post/search")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "게시글 검색")
  public SuccessResponse<Pageable> searchPost(
      @RequestBody @Validated PostSearchRequest postSearchRequest) {
    return new SuccessResponse(HttpStatus.OK.value(), "조회 성공", postReadService.search(
        postSearchRequest));
  }

  @PostMapping("/post/anonymous")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "익명 게시글 수정(쓰기) 권한 확인")
  public SuccessResponse<Boolean> checkAnonymousPostWriteAccess(
      @RequestBody @Validated PostAccessCheckDto.AnonymousPostAccessCheckDto anonymousPostAccessCheckDto) {
    return new SuccessResponse(HttpStatus.OK.value(), "권한 승인",
        postReadService.checkAnonymousPostWriteAccess(anonymousPostAccessCheckDto));
  }

  @GetMapping("/post/manage/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 관리 권한 확인")
  public SuccessResponse isOwnerOrHasManageAuthority(@PathVariable(value = "id") Long id) {
    postReadService.validatePostManageAuthority(id);
    return new SuccessResponse(HttpStatus.OK.value(), "권한이 검증되었습니다.");
  }
}
