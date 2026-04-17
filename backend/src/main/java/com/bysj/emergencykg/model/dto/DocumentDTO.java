package com.bysj.emergencykg.model.dto;

import com.bysj.emergencykg.common.PageQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class DocumentDTO {
    @Data
    public static class DocumentQueryDTO extends PageQuery {
        private Long regionId;
        private Integer preprocessStatus;
        private Integer extractionStatus;
        private String keyword;
    }
    @Data
    public static class DocumentSaveDTO {
        private Long id;
        @NotBlank(message = "预案标题不能为空")
        private String title;
        @NotNull(message = "请选择所属区域")
        private Long regionId;
        private Integer planYear;
        private String planType;
        private String sourceUrl;
        private Long fileId;
        private String fileName;
        private String filePath;
        private String content;
        private String publishOrg;
        private String approvalDate;
        private String versionNo;
        private String summary;
    }
    @Data
    public static class TaskQueryDTO extends PageQuery {
        private Long documentId;
        private Integer taskStatus;
        private String keyword;
    }
    @Data
    public static class TaskSaveDTO {
        private Long id;
        @NotNull(message = "请选择预案文档")
        private Long documentId;
        @NotBlank(message = "任务名称不能为空")
        private String taskName;
        private String modelName;
        private String promptTemplate;
    }
}
