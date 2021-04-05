package com.se.apiserver.v1.menu.infra.api;

import com.se.apiserver.v1.common.infra.dto.SuccessResponse;
import com.se.apiserver.v1.menu.application.service.MenuCreateService;
import com.se.apiserver.v1.menu.application.service.MenuDeleteService;
import com.se.apiserver.v1.menu.application.service.MenuReadService;
import com.se.apiserver.v1.menu.application.service.MenuUpdateService;
import com.se.apiserver.v1.menu.application.dto.MenuCreateDto;
import com.se.apiserver.v1.menu.application.dto.MenuReadDto;
import com.se.apiserver.v1.menu.application.dto.MenuUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "메뉴 관리")
public class MenuApiController {
    private final MenuCreateService menuCreateService;
    private final MenuUpdateService menuUpdateService;
    private final MenuDeleteService menuDeleteService;
    private final MenuReadService menuReadService;

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @PostMapping(path = "/menu")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "메뉴 생성")
    public SuccessResponse<Long> createMenu(@RequestBody @Validated MenuCreateDto.Request request) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "메뉴 등록에 성공했습니다", menuCreateService.create(request));
    }

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @PutMapping(path = "/menu")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 수정")
    public SuccessResponse<Long> updateMenu(@RequestBody @Validated MenuUpdateDto.Request request) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "메뉴 수정에 성공했습니다",
                menuUpdateService.update(request));
    }

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @DeleteMapping(path = "/menu/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 삭제")
    public SuccessResponse deleteMenu(@PathVariable(value = "id")  @Min(1) Long id) {
        menuDeleteService.delete(id);
        return new SuccessResponse(HttpStatus.CREATED.value(), "성공적으로 삭제되었습니다.");
    }

    @PreAuthorize("hasAuthority('MENU_MANAGE')")
    @GetMapping(path = "/menu/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 정보 조회")
    public SuccessResponse<MenuReadDto.ReadResponse> readMenu(@PathVariable(value = "id")  @Min(1) Long id) {
        return new SuccessResponse(HttpStatus.CREATED.value(), "성공적으로 삭제되었습니다.", menuReadService.read(id));
    }

    @GetMapping(path = "/menu")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "메뉴 전체 조회")
    public SuccessResponse<List<MenuReadDto.ReadAllResponse>> readAllMenu() {
        return new SuccessResponse(HttpStatus.CREATED.value(), "성공적으로 조회되었습니다.", menuReadService.readAll());
    }
}
