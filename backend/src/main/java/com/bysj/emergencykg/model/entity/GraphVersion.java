package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("graph_version")
public class GraphVersion {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String versionName;

    private String versionNo;

    private String sourceDesc;

    private Integer nodeCount;

    private Integer relationCount;

    private Integer tripleCount;

    private BigDecimal qualityScore;

    private Integer publishedStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
