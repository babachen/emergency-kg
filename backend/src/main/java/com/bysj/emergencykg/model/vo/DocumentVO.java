package com.bysj.emergencykg.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

public class DocumentVO {
    @Data
    public static class FileVO { private Long id; private String originalName; private String fileUrl; private String filePath; private Long fileSize; }
    @Data
    public static class DocumentItemVO { private Long id; private String title; private Long regionId; private String regionName; private Integer planYear; private String planType; private String sourceUrl; private Long fileId; private String fileName; private String filePath; private Integer preprocessStatus; private String preprocessStatusText; private Integer extractionStatus; private String extractionStatusText; private String publishOrg; private String approvalDate; private String versionNo; private String summary; private Integer sectionCount; private String content; private LocalDateTime createTime; }
    @Data
    public static class SectionVO { private Long id; private Long documentId; private String documentTitle; private String sectionNo; private String sectionTitle; private Integer sectionLevel; private String sectionContent; private Integer wordCount; }
    @Data
    public static class TaskVO { private Long id; private Long documentId; private String documentTitle; private String taskName; private String modelName; private String taskStatusText; private Integer taskStatus; private Integer progressPercent; private Integer extractedCount; private LocalDateTime startedAt; private LocalDateTime finishedAt; private String errorMessage; private LocalDateTime createTime; }
}
