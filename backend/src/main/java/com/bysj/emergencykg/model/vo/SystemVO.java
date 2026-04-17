package com.bysj.emergencykg.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SystemVO {
    @Data
    public static class CurrentUserVO {
        private Long id; private String username; private String realName; private String phone; private String email; private String avatar; private Integer status; private List<String> roleCodes = new ArrayList<>(); private List<String> roleNames = new ArrayList<>(); private boolean admin;
    }
    @Data
    public static class UserVO {
        private Long id; private String username; private String realName; private String phone; private String email; private String avatar; private Integer status; private String statusText; private String remark; private LocalDateTime lastLoginTime; private LocalDateTime createTime; private List<Long> roleIds = new ArrayList<>(); private List<String> roleNames = new ArrayList<>();
    }
    @Data
    public static class RoleVO {
        private Long id; private String roleCode; private String roleName; private String description; private Integer status; private String statusText; private List<Long> menuIds = new ArrayList<>();
    }
    @Data
    public static class MenuVO {
        private Long id; private Long parentId; private String menuName; private String menuType; private String routePath; private String componentPath; private String icon; private String permissionCode; private Integer sortOrder; private Integer visible; private Integer status; private List<MenuVO> children = new ArrayList<>();
    }
    @Data
    public static class LogVO {
        private Long id; private String moduleName; private String operationType; private String operatorName; private String content; private String ipAddress; private LocalDateTime createTime;
    }
    @Data
    public static class DictVO {
        private Long id; private String dictTypeCode; private String dictLabel; private String dictValue; private String cssType; private Integer sortOrder; private Integer status;
    }
}
