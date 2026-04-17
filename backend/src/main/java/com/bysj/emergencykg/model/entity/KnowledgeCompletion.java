package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("knowledge_completion")
public class KnowledgeCompletion {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long documentId;

    private String completionType;

    private String missingSubject;

    private String missingPredicate;

    private String missingObject;

    private String suggestionContent;

    private Integer status;

    private String applyResult;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
