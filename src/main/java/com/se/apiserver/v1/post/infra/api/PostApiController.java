package com.se.apiserver.v1.post.infra.api;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.common.presentation.response.PaginationResponse;
import com.se.apiserver.v1.common.presentation.response.Response;
import com.se.apiserver.v1.post.application.dto.*;
import com.se.apiserver.v1.post.application.dto.PostReadDto.PostSearchRequest;
import com.se.apiserver.v1.post.application.dto.request.AnnouncementPaginationRequest;
import com.se.apiserver.v1.post.application.service.PostCreateService;
import com.se.apiserver.v1.post.application.service.PostDeleteService;
import com.se.apiserver.v1.post.application.service.PostReadService;
import com.se.apiserver.v1.post.application.service.PostUpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "게시글 관리")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostApiController {

  private final PostCreateService postCreateService;
  private final PostUpdateService postUpdateService;
  private final PostReadService postReadService;
  private final PostDeleteService postDeleteService;

  @PostMapping(value = "/post")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "게시글 생성")
  public SuccessResponse<Long> create
      (@RequestPart(value = "key") @Validated PostCreateDto.Request request,
          @RequestPart(value = "files", required = false) MultipartFile[] files) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다",
        postCreateService.create(request, files));
  }

  @PutMapping("/post")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 수정")
  public SuccessResponse<Long> update(
      @RequestPart(value = "key") @Validated PostUpdateDto.Request request,
      @RequestPart(value = "files", required = false) MultipartFile[] files) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 수정되었습니다",
        postUpdateService.update(request, files));
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
  public SuccessResponse<PostReadDto.PostListResponse> readSecret(PageRequest pageRequest,
      Long boardId) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
        postReadService.readBoardPostList(pageRequest.of(), boardId));
  }

  @GetMapping("/post/announcement")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시판에 따른 공지 목록 조회")
  public PaginationResponse<List<PostAnnouncementDto>> readAllAnnouncement(
      AnnouncementPaginationRequest<Long> announcementPaginationRequest, Long boardId) {

    Page<PostAnnouncementDto> page = postReadService
        .readAnnouncementList(announcementPaginationRequest.of(), boardId);
    return new PaginationResponse<>(HttpStatus.OK, "성공적으로 조회되었습니다.", page.getContent(),
        page.getContent().size(),
        page.getTotalPages(), page.getPageable().getPageNumber(), page.getNumberOfElements());
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
