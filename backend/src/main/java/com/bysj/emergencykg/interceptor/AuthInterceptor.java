package com.bysj.emergencykg.interceptor;

import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.model.entity.SysUser;
import com.bysj.emergencykg.service.SystemService;
import com.bysj.emergencykg.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final List<String> EXCLUDE_PATHS = List.of("/api/auth/login", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/uploads/**");
    private final JwtUtil jwtUtil;
    private final SystemService systemService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    public AuthInterceptor(JwtUtil jwtUtil, SystemService systemService) { this.jwtUtil = jwtUtil; this.systemService = systemService; }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || isExcluded(request.getRequestURI())) { return true; }
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) { throw new BusinessException(401, "未登录或登录已过期"); }
        Long userId = jwtUtil.parseUserId(authorization.substring(7));
        SysUser user = systemService.getUserById(userId);
        if (user == null || user.getStatus() == null || user.getStatus() != 1) { throw new BusinessException(401, "用户状态异常，请重新登录"); }
        UserContext.set(new UserContext.UserSession(user.getId(), user.getUsername(), user.getRealName()));
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { UserContext.clear(); }
    private boolean isExcluded(String requestUri) { return EXCLUDE_PATHS.stream().anyMatch(pattern -> antPathMatcher.match(pattern, requestUri)); }
}
