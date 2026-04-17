package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kg_entity")
public class KgEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String entityName;

    private Long entityTypeId;

    private Long regionId;

    private Long sourceDocumentId;

    private String description;

    private BigDecimal confidence;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
