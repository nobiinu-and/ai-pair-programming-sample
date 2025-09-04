package com.example.aipairprogrammingsample.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 最適化レスポンスDTO
 */
public class OptimizeResponse {

    private List<Assignment> assignments;
    private List<UUID> companiesUsed;
    private Metrics metrics;

    public OptimizeResponse() {
        this.assignments = new ArrayList<>();
        this.companiesUsed = new ArrayList<>();
        this.metrics = new Metrics(0, 0);
    }

    public OptimizeResponse(List<Assignment> assignments, List<UUID> companiesUsed, Metrics metrics) {
        if (assignments == null) {
            this.assignments = new ArrayList<>();
        } else {
            this.assignments = new ArrayList<>(assignments);
        }
        if (companiesUsed == null) {
            this.companiesUsed = new ArrayList<>();
        } else {
            this.companiesUsed = new ArrayList<>(companiesUsed);
        }
        if (metrics == null) {
            this.metrics = new Metrics(0, 0);
        } else {
            this.metrics = new Metrics(metrics.getCompanyCount(), metrics.getTotalAssigned());
        }
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = new ArrayList<>(assignments);
    }

    public List<Assignment> getAssignments() {
        return new ArrayList<>(this.assignments);
    }

    public void setCompaniesUsed(List<UUID> companiesUsed) {
        this.companiesUsed = new ArrayList<>(companiesUsed);
    }

    public List<UUID> getCompaniesUsed() {
        return new ArrayList<>(this.companiesUsed);
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = new Metrics(metrics.getCompanyCount(), metrics.getTotalAssigned());
    }

    public Metrics getMetrics() {
        return new Metrics(metrics.getCompanyCount(), metrics.getTotalAssigned());
    }

    /**
     * 割り当て情報
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Assignment {
        private String metalCode;
        private UUID companyId;
        private Integer assignedQuantity;
    }

    /**
     * メトリクス情報
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Metrics {
        private Integer companyCount;
        private Integer totalAssigned;
    }
}
