package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("ai_call_record")
public class AiCallRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String bizType;

    private Long bizId;

    private String providerName;

    private String projectId;

    private String requestPrompt;

    private String responseText;

    private Integer callStatus;

    private Long durationMs;

    private String errorMessage;

    private LocalDateTime createTime;
}
