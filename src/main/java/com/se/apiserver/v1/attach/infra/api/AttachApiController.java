package com.se.apiserver.v1.attach.infra.api;

import com.se.apiserver.v1.attach.application.service.AttachCreateService;
import com.se.apiserver.v1.attach.application.service.AttachDeleteService;
import com.se.apiserver.v1.attach.application.service.AttachReadService;
import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "첨부파일 관리")
@RequestMapping("/api/v1")
public class AttachApiController {
  private final AttachCreateService attachCreateService;
  private final AttachDeleteService attachDeleteService;
  private final AttachReadService attachReadService;

  public AttachApiController(
      AttachCreateService attachCreateService,
      AttachDeleteService attachDeleteService,
      AttachReadService attachReadService) {
    this.attachCreateService = attachCreateService;
    this.attachDeleteService = attachDeleteService;
    this.attachReadService = attachReadService;
  }

  @PostMapping("/attach")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("첨부파일 생성")
  public SuccessResponse<List<AttachReadDto.Response>> create(@RequestParam(value = "files") MultipartFile... files){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다",
        attachCreateService.create(files));
  }

  @DeleteMapping("/attach/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("첨부파일 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    attachDeleteService.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
  }

  @GetMapping("/attach/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("첨부파일 조회")
  public SuccessResponse<AttachReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 삭제되었습니다", attachReadService.read(id));
  }

  @GetMapping("/attach/post/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 아이디로 게시글의 첨부파일 목록 조회")
  public SuccessResponse<List<AttachReadDto.Response>> readAllByPostId(@PathVariable(value = "id") Long postId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", attachReadService.readAllByPostId(postId));
  }

  @GetMapping("/attach/reply/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("댓글 아이디로 댓글의 첨부파일 목록 조회")
  public SuccessResponse<List<AttachReadDto.Response>> readAllByReplyId(@PathVariable(value = "id") Long replyId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", attachReadService.readAllByReplyId(replyId));
  }


  @GetMapping("/attach")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("전체 첨부파일 조회(페이징)")
  public SuccessResponse<PageImpl> readAll(PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", attachReadService.readAllByPage(pageRequest.of()));
  }

}
