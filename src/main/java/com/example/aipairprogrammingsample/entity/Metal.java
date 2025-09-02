package com.example.aipairprogrammingsample.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 金属エンティティ
 */
@Entity
@Table(name = "metal")
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

    // デフォルトコンストラクタ
    protected Metal() {}

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

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Metal{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
