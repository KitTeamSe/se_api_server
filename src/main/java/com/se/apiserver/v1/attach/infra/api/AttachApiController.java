package com.se.apiserver.v1.attach.infra.api;

import com.se.apiserver.v1.attach.domain.usecase.AttachCreateUseCase;
import com.se.apiserver.v1.attach.domain.usecase.AttachDeleteUseCase;
import com.se.apiserver.v1.attach.domain.usecase.AttachReadUseCase;
import com.se.apiserver.v1.attach.infra.dto.AttachCreateDto;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "첨부파일 관리")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AttachApiController {
  private final AttachCreateUseCase attachCreateUseCase;
  private final AttachDeleteUseCase attachDeleteUseCase;
  private final AttachReadUseCase attachReadUseCase;


  @PostMapping("/attach")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("첨부파일 생성")
  public SuccessResponse<AttachReadDto.Response> create(@RequestBody @Validated AttachCreateDto.Request request){
    return new SuccessResponse<>(HttpStatus.CREATED.value(), "성공적으로 등록되었습니다", attachCreateUseCase.create(request));
  }

  @DeleteMapping("/attach/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("첨부파일 삭제")
  public SuccessResponse delete(@PathVariable(value = "id") Long id){
    attachDeleteUseCase.delete(id);
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
  }

  @GetMapping("/attach/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("첨부파일 조회")
  public SuccessResponse<AttachReadDto.Response> read(@PathVariable(value = "id") Long id){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 삭제되었습니다", attachReadUseCase.read(id));
  }

  @GetMapping("/attach/post/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("게시글 아이디로 게시글의 첨부파일 목록 조회")
  public SuccessResponse<List<AttachReadDto.Response>> readAllByPostId(@PathVariable(value = "id") Long postId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", attachReadUseCase.readAllByPostId(postId));
  }

  @GetMapping("/attach/reply/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("댓글 아이디로 댓글의 첨부파일 목록 조회")
  public SuccessResponse<List<AttachReadDto.Response>> readAllByReplyId(@PathVariable(value = "id") Long replyId){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", attachReadUseCase.readAllByReplyId(replyId));
  }


  @GetMapping("/attach")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("전체 첨부파일 조회(페이징)")
  public SuccessResponse<PageImpl> readAll(PageRequest pageRequest){
    return new SuccessResponse<>(HttpStatus.OK.value(), "성공적으로 조회되었습니다", attachReadUseCase.readAllByPage(pageRequest.of()));
  }

}
