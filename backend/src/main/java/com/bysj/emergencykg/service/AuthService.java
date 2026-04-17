package com.bysj.emergencykg.service;

import com.bysj.emergencykg.model.dto.AuthDTO;
import com.bysj.emergencykg.model.vo.AuthVO;
import com.bysj.emergencykg.model.vo.SystemVO;

import java.util.List;
import java.util.Set;

public interface AuthService {
    AuthVO.LoginVO login(AuthDTO.LoginRequest request);
    SystemVO.CurrentUserVO currentUser();
    List<SystemVO.MenuVO> currentMenus();
    Set<String> currentPermissions();
}
