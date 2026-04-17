package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("extraction_task")
public class ExtractionTask {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long documentId;

    private String taskName;

    private String modelName;

    private String promptTemplate;

    private Integer taskStatus;

    private Integer progressPercent;

    private Integer extractedCount;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private String errorMessage;

    private Long createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
