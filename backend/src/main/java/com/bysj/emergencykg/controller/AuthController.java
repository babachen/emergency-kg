package com.bysj.emergencykg.controller;

import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.ResultUtils;
import com.bysj.emergencykg.model.dto.AuthDTO;
import com.bysj.emergencykg.model.vo.AuthVO;
import com.bysj.emergencykg.model.vo.SystemVO;
import com.bysj.emergencykg.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/login")
    public BaseResponse<AuthVO.LoginVO> login(@Valid @RequestBody AuthDTO.LoginRequest request) { return ResultUtils.success(authService.login(request)); }
    @GetMapping("/profile")
    public BaseResponse<SystemVO.CurrentUserVO> profile() { return ResultUtils.success(authService.currentUser()); }
    @GetMapping("/menus")
    public BaseResponse<List<SystemVO.MenuVO>> menus() { return ResultUtils.success(authService.currentMenus()); }
    @GetMapping("/permissions")
    public BaseResponse<Set<String>> permissions() { return ResultUtils.success(authService.currentPermissions()); }
}
