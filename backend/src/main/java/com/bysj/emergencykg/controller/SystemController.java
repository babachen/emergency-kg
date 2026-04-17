package com.bysj.emergencykg.controller;

import com.bysj.emergencykg.annotation.RequirePermission;
import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.ResultUtils;
import com.bysj.emergencykg.model.dto.SystemDTO;
import com.bysj.emergencykg.model.vo.SystemVO;
import com.bysj.emergencykg.service.SystemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system")
public class SystemController {
    private final SystemService systemService;
    public SystemController(SystemService systemService) { this.systemService = systemService; }
    @GetMapping("/users") @RequirePermission("system:user:view")
    public BaseResponse<?> users(SystemDTO.UserQueryDTO queryDTO) { return ResultUtils.success(systemService.pageUsers(queryDTO)); }
    @PostMapping("/users") @RequirePermission("system:user:add")
    public BaseResponse<Void> saveUser(@Valid @RequestBody SystemDTO.UserSaveDTO dto) { systemService.saveUser(dto); return ResultUtils.success(); }
    @PutMapping("/users/{id}") @RequirePermission("system:user:edit")
    public BaseResponse<Void> updateUser(@PathVariable Long id, @Valid @RequestBody SystemDTO.UserSaveDTO dto) { systemService.updateUser(id, dto); return ResultUtils.success(); }
    @DeleteMapping("/users/{id}") @RequirePermission("system:user:delete")
    public BaseResponse<Void> deleteUser(@PathVariable Long id) { systemService.deleteUser(id); return ResultUtils.success(); }
    @GetMapping("/roles") @RequirePermission("system:role:view")
    public BaseResponse<List<SystemVO.RoleVO>> roles() { return ResultUtils.success(systemService.listRoles()); }
    @PostMapping("/roles") @RequirePermission("system:role:add")
    public BaseResponse<Void> saveRole(@Valid @RequestBody SystemDTO.RoleSaveDTO dto) { systemService.saveRole(dto); return ResultUtils.success(); }
    @PutMapping("/roles/{id}") @RequirePermission("system:role:edit")
    public BaseResponse<Void> updateRole(@PathVariable Long id, @Valid @RequestBody SystemDTO.RoleSaveDTO dto) { systemService.updateRole(id, dto); return ResultUtils.success(); }
    @DeleteMapping("/roles/{id}") @RequirePermission("system:role:delete")
    public BaseResponse<Void> deleteRole(@PathVariable Long id) { systemService.deleteRole(id); return ResultUtils.success(); }
    @GetMapping("/menus") @RequirePermission("system:menu:view")
    public BaseResponse<List<SystemVO.MenuVO>> menus() { return ResultUtils.success(systemService.listMenus()); }
    @PostMapping("/menus") @RequirePermission("system:menu:add")
    public BaseResponse<Void> saveMenu(@Valid @RequestBody SystemDTO.MenuSaveDTO dto) { systemService.saveMenu(dto); return ResultUtils.success(); }
    @PutMapping("/menus/{id}") @RequirePermission("system:menu:edit")
    public BaseResponse<Void> updateMenu(@PathVariable Long id, @Valid @RequestBody SystemDTO.MenuSaveDTO dto) { systemService.updateMenu(id, dto); return ResultUtils.success(); }
    @DeleteMapping("/menus/{id}") @RequirePermission("system:menu:delete")
    public BaseResponse<Void> deleteMenu(@PathVariable Long id) { systemService.deleteMenu(id); return ResultUtils.success(); }
    @GetMapping("/logs") @RequirePermission("system:log:view")
    public BaseResponse<?> logs(SystemDTO.LogQueryDTO queryDTO) { return ResultUtils.success(systemService.pageLogs(queryDTO)); }
    @GetMapping("/dicts/{dictCode}")
    public BaseResponse<List<SystemVO.DictVO>> dicts(@PathVariable String dictCode) { return ResultUtils.success(systemService.listDictData(dictCode)); }
}
