package com.se.apiserver.v1.menu.infra.api;

import com.se.apiserver.v1.account.infra.dto.AccountCreateDto;
import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.menu.domain.usecase.MenuCreateUseCase;
import com.se.apiserver.v1.menu.domain.usecase.MenuDeleteUseCase;
import com.se.apiserver.v1.menu.domain.usecase.MenuReadUseCase;
import com.se.apiserver.v1.menu.domain.usecase.MenuUpdateUseCase;
import com.se.apiserver.v1.menu.infra.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.infra.dto.MenuReadDto;
import com.se.apiserver.v1.menu.infra.dto.MenuUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "메뉴 관리")
public class MenuApiController {
    private final MenuCreateUseCase menuCreateUseCase;
    private final MenuUpdateUseCase menuUpdateUseCase;
    private final MenuDeleteUseCase menuDeleteUseCase;
    private final MenuReadUseCase menuReadUseCase;

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @PostMapping(path = "/menu")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "메뉴 생성")
    public SuccessResponse<MenuCreateDto.Response> createMenu(@RequestBody @Validated MenuCreateDto.Request request) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "메뉴 등록에 성공했습니다",
                menuCreateUseCase.create(request));
    }

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @PutMapping(path = "/menu")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 수정")
    public SuccessResponse<MenuUpdateDto.Response> updateMenu(@RequestBody @Validated MenuUpdateDto.Request request) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "메뉴 수정에 성공했습니다",
                menuUpdateUseCase.update(request));
    }

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @DeleteMapping(path = "/menu/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 삭제")
    public SuccessResponse updateMenu(@PathVariable(value = "id")  @Min(1) @NotEmpty Long id) {
        menuDeleteUseCase.delete(id);
        return new SuccessResponse(HttpStatus.CREATED.value(), "성공적으로 삭제되었습니다.");
    }

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @GetMapping(path = "/menu/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 정보 조회")
    public SuccessResponse<MenuReadDto.ReadResponse> readMenu(@PathVariable(value = "id")  @Min(1) @NotEmpty Long id) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "성공적으로 삭제되었습니다.", menuReadUseCase.read(id));
    }

    @GetMapping(path = "/menu")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 정보 조회")
    public SuccessResponse<List<MenuReadDto.ReadAllResponse>> readAllMenu(@PathVariable(value = "id")  @Min(1) @NotEmpty Long id) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "성공적조회되었습니다.", menuReadUseCase.readAll());
    }
}
