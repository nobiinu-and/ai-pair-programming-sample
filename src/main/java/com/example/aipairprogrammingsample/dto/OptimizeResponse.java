package com.example.aipairprogrammingsample.dto;

import java.util.List;
import java.util.UUID;

/**
 * 最適化レスポンスDTO
 */
public class OptimizeResponse {

    private List<Assignment> assignments;
    private List<UUID> companiesUsed;
    private Metrics metrics;

    // デフォルトコンストラクタ
    public OptimizeResponse() {}

    // コンストラクタ
    public OptimizeResponse(List<Assignment> assignments, List<UUID> companiesUsed, Metrics metrics) {
        this.assignments = assignments;
        this.companiesUsed = companiesUsed;
        this.metrics = metrics;
    }

    // Getters and Setters
    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<UUID> getCompaniesUsed() {
        return companiesUsed;
    }

    public void setCompaniesUsed(List<UUID> companiesUsed) {
        this.companiesUsed = companiesUsed;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * 割り当て情報
     */
    public static class Assignment {
        private String metalCode;
        private UUID companyId;
        private Integer assignedQuantity;

        // デフォルトコンストラクタ
        public Assignment() {}

        // コンストラクタ
        public Assignment(String metalCode, UUID companyId, Integer assignedQuantity) {
            this.metalCode = metalCode;
            this.companyId = companyId;
            this.assignedQuantity = assignedQuantity;
        }

        // Getters and Setters
        public String getMetalCode() {
            return metalCode;
        }

        public void setMetalCode(String metalCode) {
            this.metalCode = metalCode;
        }

        public UUID getCompanyId() {
            return companyId;
        }

        public void setCompanyId(UUID companyId) {
            this.companyId = companyId;
        }

        public Integer getAssignedQuantity() {
            return assignedQuantity;
        }

        public void setAssignedQuantity(Integer assignedQuantity) {
            this.assignedQuantity = assignedQuantity;
        }
    }

    /**
     * メトリクス情報
     */
    public static class Metrics {
        private Integer companyCount;
        private Integer totalAssigned;

        // デフォルトコンストラクタ
        public Metrics() {}

        // コンストラクタ
        public Metrics(Integer companyCount, Integer totalAssigned) {
            this.companyCount = companyCount;
            this.totalAssigned = totalAssigned;
        }

        // Getters and Setters
        public Integer getCompanyCount() {
            return companyCount;
        }

        public void setCompanyCount(Integer companyCount) {
            this.companyCount = companyCount;
        }

        public Integer getTotalAssigned() {
            return totalAssigned;
        }

        public void setTotalAssigned(Integer totalAssigned) {
            this.totalAssigned = totalAssigned;
        }
    }
}
