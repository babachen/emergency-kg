package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("knowledge_conflict")
public class KnowledgeConflict {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tripleId;

    private String conflictType;

    private String conflictDesc;

    private Integer status;

    private String suggestedResolution;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
