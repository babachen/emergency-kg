package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kg_relation")
public class KgRelation {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long subjectEntityId;

    private Long relationTypeId;

    private Long objectEntityId;

    private Long sourceDocumentId;

    private String relationDesc;

    private BigDecimal confidence;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
