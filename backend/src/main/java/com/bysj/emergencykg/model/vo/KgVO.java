package com.bysj.emergencykg.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KgVO {
    @Data public static class OntologyVO { private Long id; private String code; private String name; private String color; private String description; }
    @Data public static class EntityVO { private Long id; private String entityName; private Long entityTypeId; private String entityTypeName; private Long regionId; private String regionName; private Long sourceDocumentId; private String sourceDocumentTitle; private String description; private BigDecimal confidence; private Integer status; private String statusText; private LocalDateTime createTime; }
    @Data public static class TripleVO { private Long id; private String subjectName; private String predicateName; private String objectName; private Long sourceDocumentId; private String sourceDocumentTitle; private Long regionId; private String regionName; private Long versionId; private String versionName; private BigDecimal confidence; private Integer validationStatus; private String validationStatusText; private LocalDateTime createTime; }
    @Data public static class GraphNodeVO { private String id; private String name; private String category; private Long sourceId; private Integer symbolSize; }
    @Data public static class GraphLinkVO { private String source; private String target; private String value; }
    @Data public static class GraphVO { private List<GraphNodeVO> nodes = new ArrayList<>(); private List<GraphLinkVO> links = new ArrayList<>(); private List<Map<String, Object>> cypherRows = new ArrayList<>(); private String message; }
    @Data public static class QaResponseVO { private String answer; private List<String> reasoningChain = new ArrayList<>(); private List<String> references = new ArrayList<>(); private String modelName; }
    @Data public static class ConflictVO { private Long id; private Long tripleId; private String conflictType; private String conflictDesc; private Integer status; private String statusText; private String suggestedResolution; private LocalDateTime createTime; }
    @Data public static class CompletionVO { private Long id; private Long documentId; private String documentTitle; private String completionType; private String missingSubject; private String missingPredicate; private String missingObject; private String suggestionContent; private Integer status; private String statusText; private String applyResult; private LocalDateTime createTime; }
    @Data public static class VersionVO { private Long id; private String versionName; private String versionNo; private String sourceDesc; private Integer nodeCount; private Integer relationCount; private Integer tripleCount; private BigDecimal qualityScore; private Integer publishedStatus; private String publishedStatusText; private LocalDateTime createTime; }
}
