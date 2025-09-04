package com.example.aipairprogrammingsample.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 金属エンティティ
 */
@Entity
@Table(name = "metal")
@Data
@NoArgsConstructor
public class Metal {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 10)
    private String unit;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // コンストラクタ
    public Metal(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Metal(String code, String name, String unit) {
        this.code = code;
        this.name = name;
        this.unit = unit;
    }
}
