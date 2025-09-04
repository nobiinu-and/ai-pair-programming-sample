package com.example.aipairprogrammingsample.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 保有量エンティティ
 */
@Entity
@Table(name = "holding")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_metal_id", nullable = false)
    @SuppressFBWarnings("EI_EXPOSE_REP") // Entityのため、やむを得ず
    private CompanyMetal companyMetal;

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // コンストラクタ
    public Holding(CompanyMetal companyMetal, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.companyMetal = companyMetal;
        this.quantity = quantity;
    }
    
    // CT_CONSTRUCTOR_THROWで指摘された脆弱性抑止のために何もしないFinalizeメソッドをfinalとして定義
    protected final void finalize() {
        // Do nothing
    }
}
