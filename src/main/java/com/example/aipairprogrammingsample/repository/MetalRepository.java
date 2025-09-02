package com.example.aipairprogrammingsample.repository;

import com.example.aipairprogrammingsample.entity.Metal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 金属リポジトリ
 */
@Repository
public interface MetalRepository extends JpaRepository<Metal, UUID> {
    
    /**
     * 金属コードで検索
     * @param code 金属コード
     * @return 金属エンティティ
     */
    Optional<Metal> findByCode(String code);
    
    /**
     * 金属コードが存在するかチェック
     * @param code 金属コード
     * @return 存在する場合true
     */
    boolean existsByCode(String code);
}
