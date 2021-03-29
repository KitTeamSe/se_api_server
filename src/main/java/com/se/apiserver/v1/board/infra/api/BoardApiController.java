package com.se.apiserver.v1.board.infra.api;

import com.se.apiserver.v1.board.domain.usecase.BoardCreateUseCase;
import com.se.apiserver.v1.board.domain.usecase.BoardDeleteUseCase;
import com.se.apiserver.v1.board.domain.usecase.BoardReadUseCase;
import com.se.apiserver.v1.board.domain.usecase.BoardUpdateUseCase;
import com.se.apiserver.v1.board.infra.dto.BoardCreateDto;
import com.se.apiserver.v1.board.infra.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.dto.BoardUpdateDto;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "게시판 관리")
public class BoardApiController {

    private final BoardReadUseCase boardReadUseCase;
    private final BoardCreateUseCase boardCreateUseCase;
    private final BoardUpdateUseCase boardUpdateUseCase;
    private final BoardDeleteUseCase boardDeleteUseCase;

    @GetMapping(value = "/board/{id}")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 조회")
    public SuccessResponse<BoardReadDto.ReadResponse> read(@ApiParam(value = "게시판 아이디",example = "1") @PathVariable(name = "id") Long id){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다", boardReadUseCase.read(id));
    }

    @GetMapping(value = "/board")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 전체 조회")
    public SuccessResponse<List<BoardReadDto.ReadResponse>> readAll(){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다", boardReadUseCase.readAll());
    }

    @PutMapping(value = "/board")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 수정")
    public SuccessResponse<BoardReadDto.ReadResponse> update(@RequestBody @Validated BoardUpdateDto.Request request){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다", boardUpdateUseCase.update(request));
    }

    @DeleteMapping(value = "/board/{id}")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 삭제")
    public SuccessResponse update(@ApiParam(value = "게시판 아이디", example = "1") @PathVariable(value = "id") Long id){
        boardDeleteUseCase.delete(id);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
    }

    @PostMapping(value = "/board")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 등록")
    public SuccessResponse<BoardReadDto.ReadResponse> update(@RequestBody @Validated BoardCreateDto.Request request){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 등록되었습니다", boardCreateUseCase.create(request));
    }
}
