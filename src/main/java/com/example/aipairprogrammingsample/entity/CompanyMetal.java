package com.example.aipairprogrammingsample.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 会社・金属関連エンティティ（多対多の関係を管理）
 */
@Entity
@Table(name = "company_metal",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "metal_id"}))
public class CompanyMetal {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metal_id", nullable = false)
    private Metal metal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // デフォルトコンストラクタ
    protected CompanyMetal() {}

    // コンストラクタ
    public CompanyMetal(Company company, Metal metal) {
        this.company = company;
        this.metal = metal;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Metal getMetal() {
        return metal;
    }

    public void setMetal(Metal metal) {
        this.metal = metal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "CompanyMetal{" +
                "id=" + id +
                ", company=" + (company != null ? company.getName() : null) +
                ", metal=" + (metal != null ? metal.getCode() : null) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
