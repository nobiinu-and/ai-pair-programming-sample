package com.example.aipairprogrammingsample.repository;

import com.example.aipairprogrammingsample.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 会社リポジトリ
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    
    /**
     * 会社名で検索
     * @param name 会社名
     * @return 会社エンティティ
     */
    Optional<Company> findByName(String name);
    
    /**
     * 会社名が存在するかチェック
     * @param name 会社名
     * @return 存在する場合true
     */
    boolean existsByName(String name);
}
