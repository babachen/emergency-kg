package com.bysj.emergencykg.model.dto;

import com.bysj.emergencykg.common.PageQuery;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class KgDTO {
    @Data
    public static class OntologySaveDTO {
        private Long id;
        @NotBlank(message = "类型编码不能为空")
        private String code;
        @NotBlank(message = "类型名称不能为空")
        private String name;
        private String color;
        private String description;
    }
    @Data
    public static class EntityQueryDTO extends PageQuery {
        private Long regionId;
        private Long entityTypeId;
        private String keyword;
    }
    @Data
    public static class TripleQueryDTO extends PageQuery {
        private Long documentId;
        private Long regionId;
        private Integer validationStatus;
        private String keyword;
    }
    @Data
    public static class GraphQueryDTO {
        private Long regionId;
        private Long documentId;
        private String keyword;
    }
    @Data
    public static class CypherQueryDTO {
        @NotBlank(message = "Cypher 语句不能为空")
        private String cypher;
    }
    @Data
    public static class QaRequestDTO {
        @NotBlank(message = "问题不能为空")
        private String question;
        private Long regionId;
        private Long documentId;
    }
    @Data
    public static class VersionSaveDTO {
        @NotBlank(message = "版本名称不能为空")
        private String versionName;
        @NotBlank(message = "版本号不能为空")
        private String versionNo;
        private String sourceDesc;
    }
}
