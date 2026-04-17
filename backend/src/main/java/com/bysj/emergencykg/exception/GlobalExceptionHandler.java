package com.bysj.emergencykg.exception;

import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.ResultUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Void> handleBusiness(BusinessException e) { return ResultUtils.fail(e.getCode(), e.getMessage()); }
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public BaseResponse<Void> handleValidation(Exception e) { return ResultUtils.fail(400, "参数校验失败：" + e.getMessage()); }
    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleException(Exception e) { log.error("系统异常", e); return ResultUtils.fail(500, "系统繁忙，请稍后重试"); }
}
