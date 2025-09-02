package com.example.aipairprogrammingsample.repository;

import com.example.aipairprogrammingsample.entity.Holding;
import com.example.aipairprogrammingsample.entity.CompanyMetal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 保有量リポジトリ
 */
@Repository
public interface HoldingRepository extends JpaRepository<Holding, UUID> {
    
    /**
     * 会社・金属関連で保有量を検索
     * @param companyMetal 会社・金属関連
     * @return 保有量エンティティ
     */
    Optional<Holding> findByCompanyMetal(CompanyMetal companyMetal);
    
    /**
     * 特定の金属コードの保有量を作成日時順（早い順）で取得
     * @param metalCode 金属コード
     * @return 保有量エンティティのリスト
     */
    @Query("SELECT h FROM Holding h " +
           "JOIN h.companyMetal cm " +
           "JOIN cm.metal m " +
           "WHERE m.code = :metalCode AND h.quantity > 0 " +
           "ORDER BY cm.createdAt ASC")
    List<Holding> findByMetalCodeWithPositiveQuantityOrderByCreatedAtAsc(@Param("metalCode") String metalCode);
    
    /**
     * 特定の金属コードの合計保有量を取得
     * @param metalCode 金属コード
     * @return 合計保有量
     */
    @Query("SELECT COALESCE(SUM(h.quantity), 0) FROM Holding h " +
           "JOIN h.companyMetal cm " +
           "JOIN cm.metal m " +
           "WHERE m.code = :metalCode")
    Integer getTotalQuantityByMetalCode(@Param("metalCode") String metalCode);
}
