package com.example.aipairprogrammingsample.repository;

import com.example.aipairprogrammingsample.entity.CompanyMetal;
import com.example.aipairprogrammingsample.entity.Company;
import com.example.aipairprogrammingsample.entity.Metal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 会社・金属関連リポジトリ
 */
@Repository
public interface CompanyMetalRepository extends JpaRepository<CompanyMetal, UUID> {
    
    /**
     * 会社と金属の組み合わせで検索
     * @param company 会社
     * @param metal 金属
     * @return 会社・金属関連エンティティ
     */
    Optional<CompanyMetal> findByCompanyAndMetal(Company company, Metal metal);
    
    /**
     * 特定の会社に関連する金属をすべて取得
     * @param company 会社
     * @return 会社・金属関連エンティティのリスト
     */
    List<CompanyMetal> findByCompany(Company company);
    
    /**
     * 特定の金属を扱う会社をすべて取得
     * @param metal 金属
     * @return 会社・金属関連エンティティのリスト
     */
    List<CompanyMetal> findByMetal(Metal metal);
    
    /**
     * 特定の金属コードを扱う会社を作成日時順（早い順）で取得
     * @param metalCode 金属コード
     * @return 会社・金属関連エンティティのリスト
     */
    @Query("SELECT cm FROM CompanyMetal cm JOIN cm.metal m WHERE m.code = :metalCode ORDER BY cm.createdAt ASC")
    List<CompanyMetal> findByMetalCodeOrderByCreatedAtAsc(@Param("metalCode") String metalCode);
}
