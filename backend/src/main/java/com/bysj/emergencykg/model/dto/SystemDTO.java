package com.bysj.emergencykg.model.dto;

import com.bysj.emergencykg.common.PageQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class SystemDTO {
    @Data
    public static class UserQueryDTO extends PageQuery {
        private String keyword;
        private Integer status;
    }
    @Data
    public static class UserSaveDTO {
        private Long id;
        @NotBlank(message = "用户名不能为空")
        private String username;
        private String password;
        @NotBlank(message = "姓名不能为空")
        private String realName;
        private String phone;
        private String email;
        private String avatar;
        @NotNull(message = "状态不能为空")
        private Integer status;
        private String remark;
        @NotEmpty(message = "请选择角色")
        private List<Long> roleIds = new ArrayList<>();
    }
    @Data
    public static class RoleSaveDTO {
        private Long id;
        @NotBlank(message = "角色编码不能为空")
        private String roleCode;
        @NotBlank(message = "角色名称不能为空")
        private String roleName;
        private String description;
        @NotNull(message = "状态不能为空")
        private Integer status;
        private List<Long> menuIds = new ArrayList<>();
    }
    @Data
    public static class MenuSaveDTO {
        private Long id;
        @NotBlank(message = "菜单名称不能为空")
        private String menuName;
        @NotBlank(message = "菜单类型不能为空")
        private String menuType;
        private Long parentId = 0L;
        private String routePath;
        private String componentPath;
        private String icon;
        private String permissionCode;
        private Integer sortOrder = 0;
        private Integer visible = 1;
        private Integer status = 1;
    }
    @Data
    public static class LogQueryDTO extends PageQuery {
        private String moduleName;
        private String operationType;
    }
}
