package com.bysj.emergencykg.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDTO {
    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
    }
}
