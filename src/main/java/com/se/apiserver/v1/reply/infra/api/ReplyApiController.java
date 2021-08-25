package com.se.apiserver.v1.reply.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.common.presentation.response.PaginationResponse;
import com.se.apiserver.v1.reply.application.dto.ReplyCreateDto;
import com.se.apiserver.v1.reply.application.dto.ReplyDeleteDto;
import com.se.apiserver.v1.reply.application.dto.ReplyReadDto;
import com.se.apiserver.v1.reply.application.dto.ReplyUpdateDto;
import com.se.apiserver.v1.reply.application.dto.request.ReplyPaginationRequest;
import com.se.apiserver.v1.reply.application.service.ReplyCreateService;
import com.se.apiserver.v1.reply.application.service.ReplyDeleteService;
import com.se.apiserver.v1.reply.application.service.ReplyReadService;
import com.se.apiserver.v1.reply.application.service.ReplyUpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "댓글 관리")
public class ReplyApiController {

  private final ReplyCreateService replyCreateService;
  private final ReplyDeleteService replyDeleteService;
  private final ReplyUpdateService replyUpdateService;
  private final ReplyReadService replyReadService;

  @PostMapping(value = "/reply")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "댓글 생성")
  public SuccessResponse<Long> create(
      @RequestPart(value = "key") @Validated ReplyCreateDto.Request request,
      @RequestPart(value = "files", required = false) MultipartFile[] files) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다",
        replyCreateService.create(request, files));
  }

  @PutMapping("/reply")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("댓글 수정")
  public SuccessResponse<Long> update(
      @RequestPart(value = "key") @Validated ReplyUpdateDto.Request request,
      @RequestPart(value = "files", required = false) MultipartFile[] files) {
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 수정되었습니다",
        replyUpdateService.update(request, files));
  }

  @DeleteMapping("/reply/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("댓글 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long replyId) {
    replyDeleteService.delete(replyId);
    return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
  }

  @DeleteMapping("/reply/anonymous")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("익명 댓글 삭제")
  public SuccessResponse delete(
      @RequestBody @Validated ReplyDeleteDto.AnonymousReplyDeleteRequest anonymousReplyDeleteRequest) {
    replyDeleteService.deleteAnonymousReply(anonymousReplyDeleteRequest);
    return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
  }

  @GetMapping("/reply/{reply_id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("댓글 단일 조회")
  public SuccessResponse<ReplyReadDto.Response> read(
      @PathVariable(value = "reply_id") Long replyId) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
        replyReadService.read(replyId));
  }

  @GetMapping("/reply/secret")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("익명 비밀 댓글 조회")
  public SuccessResponse<ReplyReadDto.Response> readSecret(Long replyId, String password) {
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다",
        replyReadService.readAnonymousSecretReply(replyId, password));
  }

  @GetMapping("/reply/post/{post_id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("댓글 리스트 조회")
  public PaginationResponse<List<ReplyReadDto.Response>> readAllBelongPost(
      @PathVariable(value = "post_id") Long postId,
      @ModelAttribute ReplyPaginationRequest replyPaginationRequest) {
    ReplyReadDto.ResponseListWithPage response = replyReadService
        .readAllBelongPost(postId, replyPaginationRequest.of());
    return new PaginationResponse<>(HttpStatus.OK, "성공적으로 조회되었습니다"
        , response.getResponseList(), response.getTotalData(),
        response.getTotalPage(), response.getCurrentPage(), response.getPerPage());
  }
}
