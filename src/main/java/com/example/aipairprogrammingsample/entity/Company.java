package com.example.aipairprogrammingsample.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 会社エンティティ
 */
@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String contact;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // コンストラクタ
    public Company(String name) {
        this.name = name;
    }

    public Company(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }
}
