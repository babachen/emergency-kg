package com.bysj.emergencykg.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

public class AuthVO {
    @Data
    public static class LoginVO {
        private String token;
        private SystemVO.CurrentUserVO userInfo;
        private List<SystemVO.MenuVO> menus;
        private Set<String> permissions;
    }
}
