package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("plan_document")
public class PlanDocument {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private Long regionId;

    private Integer planYear;

    private String planType;

    private String sourceUrl;

    private Long fileId;

    private String fileName;

    private String filePath;

    private String content;

    private Integer preprocessStatus;

    private Integer extractionStatus;

    private String publishOrg;

    private String approvalDate;

    private String versionNo;

    private String summary;

    private Long createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
