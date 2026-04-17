package com.bysj.emergencykg.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DashboardVO {
    private long documentCount; private long sectionCount; private long taskCount; private long runningTaskCount; private long entityCount; private long relationCount; private long tripleCount; private long conflictCount; private long completionCount; private long versionCount;
    private List<NameValueVO> taskStatusList = new ArrayList<>();
    private List<NameValueVO> regionDistribution = new ArrayList<>();
    private List<NameValueVO> qualityTrend = new ArrayList<>();
    @Data
    public static class NameValueVO {
        private String name; private Number value;
        public NameValueVO() {}
        public NameValueVO(String name, Number value) { this.name = name; this.value = value; }
    }
}
