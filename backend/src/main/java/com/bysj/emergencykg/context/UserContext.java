package com.bysj.emergencykg.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class UserContext {
    private static final ThreadLocal<UserSession> HOLDER = new ThreadLocal<>();
    private UserContext() {}
    public static void set(UserSession userSession) { HOLDER.set(userSession); }
    public static UserSession get() { return HOLDER.get(); }
    public static Long getUserId() { return HOLDER.get() == null ? null : HOLDER.get().getUserId(); }
    public static void clear() { HOLDER.remove(); }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSession {
        private Long userId;
        private String username;
        private String realName;
    }
}
