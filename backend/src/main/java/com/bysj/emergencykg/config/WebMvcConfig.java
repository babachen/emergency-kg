package com.bysj.emergencykg.config;

import com.bysj.emergencykg.annotation.RequirePermission;
import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.interceptor.AuthInterceptor;
import com.bysj.emergencykg.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final AppProperties appProperties;
    private final SystemService systemService;
    public WebMvcConfig(AuthInterceptor authInterceptor, AppProperties appProperties, SystemService systemService) { this.authInterceptor = authInterceptor; this.appProperties = appProperties; this.systemService = systemService; }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        registry.addInterceptor(new org.springframework.web.servlet.HandlerInterceptor() {
            @Override
            public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) {
                if (!(handler instanceof HandlerMethod handlerMethod)) { return true; }
                RequirePermission permission = handlerMethod.getMethodAnnotation(RequirePermission.class);
                if (permission == null) { permission = handlerMethod.getBeanType().getAnnotation(RequirePermission.class); }
                if (permission == null || !StringUtils.hasText(permission.value())) { return true; }
                Long userId = UserContext.getUserId();
                if (userId == null) { throw new BusinessException(401, "未登录"); }
                if (systemService.isAdmin(userId)) { return true; }
                Set<String> permissions = systemService.getPermissionCodes(userId);
                if (!permissions.contains(permission.value())) { throw new BusinessException(403, "无权限执行该操作：" + permission.value()); }
                return true;
            }
        }).addPathPatterns("/api/**");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(true).maxAge(3600);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(appProperties.getStorage().getLocation()).toAbsolutePath();
        registry.addResourceHandler(appProperties.getStorage().getAccessPath() + "/**").addResourceLocations(uploadPath.toUri().toString());
        log.info("静态上传目录映射：{} -> {}", appProperties.getStorage().getAccessPath(), uploadPath);
    }
}
