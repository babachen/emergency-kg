package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kg_triple")
public class KgTriple {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String subjectName;

    private String predicateName;

    private String objectName;

    private Long subjectEntityId;

    private Long relationId;

    private Long objectEntityId;

    private Long sourceDocumentId;

    private Long versionId;

    private BigDecimal confidence;

    private Integer validationStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
