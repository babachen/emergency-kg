package com.bysj.emergencykg.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
