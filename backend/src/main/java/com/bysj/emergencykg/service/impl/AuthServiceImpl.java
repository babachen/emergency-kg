package com.bysj.emergencykg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.mapper.SysUserMapper;
import com.bysj.emergencykg.model.dto.AuthDTO;
import com.bysj.emergencykg.model.entity.SysUser;
import com.bysj.emergencykg.model.vo.AuthVO;
import com.bysj.emergencykg.model.vo.SystemVO;
import com.bysj.emergencykg.service.AuthService;
import com.bysj.emergencykg.service.OperationLogService;
import com.bysj.emergencykg.service.SystemService;
import com.bysj.emergencykg.utils.JwtUtil;
import com.bysj.emergencykg.utils.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final SysUserMapper sysUserMapper;
    private final PasswordUtils passwordUtils;
    private final JwtUtil jwtUtil;
    private final SystemService systemService;
    private final OperationLogService operationLogService;

    public AuthServiceImpl(SysUserMapper sysUserMapper, PasswordUtils passwordUtils, JwtUtil jwtUtil, SystemService systemService, OperationLogService operationLogService) {
        this.sysUserMapper = sysUserMapper;
        this.passwordUtils = passwordUtils;
        this.jwtUtil = jwtUtil;
        this.systemService = systemService;
        this.operationLogService = operationLogService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthVO.LoginVO login(AuthDTO.LoginRequest request) {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()).last("limit 1"));
        if (user == null || !passwordUtils.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(403, "账号已被停用");
        }
        user.setLastLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
        AuthVO.LoginVO vo = new AuthVO.LoginVO();
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getUsername()));
        vo.setUserInfo(systemService.buildCurrentUser(user.getId()));
        vo.setMenus(systemService.getCurrentUserMenus(user.getId()));
        vo.setPermissions(systemService.getPermissionCodes(user.getId()));
        operationLogService.log("认证中心", "登录", user.getRealName() + " 登录系统");
        return vo;
    }

    @Override
    public SystemVO.CurrentUserVO currentUser() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        return systemService.buildCurrentUser(userId);
    }

    @Override
    public List<SystemVO.MenuVO> currentMenus() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        return systemService.getCurrentUserMenus(userId);
    }

    @Override
    public Set<String> currentPermissions() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        return systemService.getPermissionCodes(userId);
    }
}
