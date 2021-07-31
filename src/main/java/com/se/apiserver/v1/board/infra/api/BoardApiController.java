package com.se.apiserver.v1.board.infra.api;

import com.se.apiserver.v1.board.application.service.BoardAuthorityService;
import com.se.apiserver.v1.board.application.service.BoardCreateService;
import com.se.apiserver.v1.board.application.service.BoardDeleteService;
import com.se.apiserver.v1.board.application.service.BoardReadService;
import com.se.apiserver.v1.board.application.service.BoardUpdateService;
import com.se.apiserver.v1.board.application.dto.BoardCreateDto;
import com.se.apiserver.v1.board.application.dto.BoardReadDto;
import com.se.apiserver.v1.board.application.dto.BoardUpdateDto;
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

    private final BoardReadService boardReadService;
    private final BoardCreateService boardCreateService;
    private final BoardUpdateService boardUpdateService;
    private final BoardDeleteService boardDeleteService;
    private final BoardAuthorityService boardAuthorityService;

    @GetMapping(value = "/board/{nameEng}")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 영어 이름 조회")
    public SuccessResponse<BoardReadDto.ReadResponse> read(@ApiParam(value = "게시판 영어 이름",example = "freeboard") @PathVariable(name = "nameEng") String nameEng){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다", boardReadService.readByNameEng(nameEng) );
    }

    @GetMapping(value = "/board")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 전체 조회")
    public SuccessResponse<List<BoardReadDto.ReadResponse>> readAll(){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 조회되었습니다", boardReadService.readAll());
    }

    @PutMapping(value = "/board")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 수정")
    public SuccessResponse<Long> update(@RequestBody @Validated BoardUpdateDto.Request request){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 수정되었습니다", boardUpdateService.update(request));
    }

    @DeleteMapping(value = "/board/{nameEng}")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 삭제")
    public SuccessResponse update(@ApiParam(value = "게시판 영어 이름",example = "freeboard") @PathVariable(name = "nameEng") String nameEng){
        boardDeleteService.delete(nameEng);
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 삭제되었습니다");
    }

    @PostMapping(value = "/board")
    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 등록")
    public SuccessResponse<Long> update(@RequestBody @Validated BoardCreateDto.Request request){
        return new SuccessResponse(HttpStatus.OK.value(), "성공적으로 등록되었습니다", boardCreateService.create(request));
    }

    @GetMapping(value = "/board/validate/access/{nameEng}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 접근 권한 확인")
    public SuccessResponse validateAccessAuthority(@ApiParam(value = "게시판 영어 이름",example = "freeboard") @PathVariable(name = "nameEng") String nameEng){
        boardAuthorityService.validateAccessAuthority(nameEng);
        return new SuccessResponse(HttpStatus.OK.value(), "접근 권한이 검증되었습니다.");
    }

    @GetMapping(value = "/board/validate/manage/{nameEng}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "게시판 관리 권한 확인")
    public SuccessResponse validateManageAuthority(@ApiParam(value = "게시판 영어 이름",example = "freeboard") @PathVariable(name = "nameEng") String nameEng){
        boardAuthorityService.validateManageAuthority(nameEng);
        return new SuccessResponse(HttpStatus.OK.value(), "관리 권한이 검증되었습니다.");
    }
}
