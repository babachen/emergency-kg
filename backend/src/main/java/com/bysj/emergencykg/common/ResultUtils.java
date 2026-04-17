package com.bysj.emergencykg.common;

import java.time.LocalDateTime;

public final class ResultUtils {
    private ResultUtils() {}
    public static <T> BaseResponse<T> success(T data) { return new BaseResponse<>(200, "操作成功", data, LocalDateTime.now()); }
    public static BaseResponse<Void> success() { return new BaseResponse<>(200, "操作成功", null, LocalDateTime.now()); }
    public static BaseResponse<Void> fail(Integer code, String message) { return new BaseResponse<>(code, message, null, LocalDateTime.now()); }
}
