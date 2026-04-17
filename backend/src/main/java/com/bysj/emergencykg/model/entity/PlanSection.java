package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("plan_section")
public class PlanSection {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long documentId;

    private String sectionNo;

    private String sectionTitle;

    private Integer sectionLevel;

    private String sectionContent;

    private Integer wordCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
