package com.example.aipairprogrammingsample.entity;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 会社・金属関連エンティティ（多対多の関係を管理）
 */
@Entity
@Table(name = "company_metal",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "metal_id"}))
@Data
public class CompanyMetal {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @SuppressFBWarnings("EI_EXPOSE_REP") // Entityのため、やむを得ず
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metal_id", nullable = false)
    @SuppressFBWarnings("EI_EXPOSE_REP") // Entityのため、やむを得ず
    private Metal metal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // コンストラクタ
    public CompanyMetal(Company company, Metal metal) {
        this.company = company;
        this.metal = metal;
    }
}
