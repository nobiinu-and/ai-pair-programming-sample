package com.example.aipairprogrammingsample.repository;

import com.example.aipairprogrammingsample.entity.Company;
import com.example.aipairprogrammingsample.entity.CompanyMetal;
import com.example.aipairprogrammingsample.entity.Holding;
import com.example.aipairprogrammingsample.entity.Metal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * リポジトリ統合テスト
 */
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RepositoryIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MetalRepository metalRepository;

    @Autowired
    private CompanyMetalRepository companyMetalRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    // テストデータ用の変数
    private Company techMetal;
    private Company rareEarth;
    private Company globalMining;
    private Metal neodymium;
    private Metal dysprosium;
    private Metal praseodymium;
    private Metal terbium;

    @BeforeEach
    void setUp() {
        // 会社データの作成と保存
        List<Company> companies = List.of(
                new Company("TechMetal Corp", "contact@techmetal.com"),
                new Company("RareEarth Industries", "info@rareearth.com"),
                new Company("Global Mining Ltd", "sales@globalmining.com")
        );
        companies = companyRepository.saveAll(companies);
        techMetal = companies.get(0);
        rareEarth = companies.get(1);
        globalMining = companies.get(2);

        // 金属データの作成と保存
        List<Metal> metals = List.of(
                new Metal("Nd", "Neodymium", "kg"),
                new Metal("Dy", "Dysprosium", "kg"),
                new Metal("Pr", "Praseodymium", "kg"),
                new Metal("Tb", "Terbium", "kg")
        );
        metals = metalRepository.saveAll(metals);
        neodymium = metals.get(0);
        dysprosium = metals.get(1);
        praseodymium = metals.get(2);
        terbium = metals.get(3);

        // 会社・金属関連データの作成と保存
        List<CompanyMetal> companyMetals = List.of(
                // TechMetal Corp: Nd, Dy
                new CompanyMetal(techMetal, neodymium),
                new CompanyMetal(techMetal, dysprosium),
                // RareEarth Industries: Nd, Pr, Tb
                new CompanyMetal(rareEarth, neodymium),
                new CompanyMetal(rareEarth, praseodymium),
                new CompanyMetal(rareEarth, terbium),
                // Global Mining Ltd: Dy, Pr
                new CompanyMetal(globalMining, dysprosium),
                new CompanyMetal(globalMining, praseodymium)
        );
        companyMetals = companyMetalRepository.saveAll(companyMetals);

        // 保有量データの作成と保存
        List<Holding> holdings = List.of(
                // TechMetal Corp
                new Holding(companyMetals.get(0), 1000), // TechMetal - Nd
                new Holding(companyMetals.get(1), 500),  // TechMetal - Dy
                // RareEarth Industries
                new Holding(companyMetals.get(2), 800),  // RareEarth - Nd
                new Holding(companyMetals.get(3), 600),  // RareEarth - Pr
                new Holding(companyMetals.get(4), 300),  // RareEarth - Tb
                // Global Mining Ltd
                new Holding(companyMetals.get(5), 400),  // Global - Dy
                new Holding(companyMetals.get(6), 750)   // Global - Pr
        );
        holdingRepository.saveAll(holdings);
    }    
    
    @Test
    void テストで設定した会社と金属のデータが正しく取得できること() {
        // テストで設定されたデータを確認
        var companies = companyRepository.findAll();
        assertThat(companies).hasSize(3);
        assertThat(companies)
                .extracting(Company::getName)
                .containsExactlyInAnyOrder(
                        "TechMetal Corp", 
                        "RareEarth Industries", 
                        "Global Mining Ltd"
                );

        var metals = metalRepository.findAll();
        assertThat(metals).hasSize(4);
        assertThat(metals)
                .extracting(Metal::getCode)
                .containsExactlyInAnyOrder("Nd", "Dy", "Pr", "Tb");
    }

    @Test
    void 金属コードで金属情報が正しく検索できること() {
        var neodymium = metalRepository.findByCode("Nd");
        assertThat(neodymium).isPresent();
        assertThat(neodymium.get().getName()).isEqualTo("Neodymium");
        assertThat(neodymium.get().getUnit()).isEqualTo("kg");
    }

    @Test
    void 会社名で会社情報が正しく検索できること() {
        var techMetal = companyRepository.findByName("TechMetal Corp");
        assertThat(techMetal).isPresent();
        assertThat(techMetal.get().getContact()).isEqualTo("contact@techmetal.com");
    }

    @Test
    void 金属コード別の合計保有量が正しく計算されること() {
        // Ndの合計保有量を確認（TechMetal Corp: 1000kg + RareEarth Industries: 800kg = 1800kg）
        var totalNdQuantity = holdingRepository.getTotalQuantityByMetalCode("Nd");
        assertThat(totalNdQuantity).isEqualTo(1800);

        // Dyの合計保有量を確認（TechMetal Corp: 500kg + Global Mining Ltd: 400kg = 900kg）
        var totalDyQuantity = holdingRepository.getTotalQuantityByMetalCode("Dy");
        assertThat(totalDyQuantity).isEqualTo(900);
    }

    @Test
    void 金属コード別の保有量データが登録日時順で正しく取得されること() {
        // Ndを扱う会社の保有量を登録日時順で取得
        var ndHoldings = holdingRepository.findByMetalCodeWithPositiveQuantityOrderByCreatedAtAsc("Nd");
        assertThat(ndHoldings).hasSize(2);
        
        // 全て正の値であることを確認
        ndHoldings.forEach(holding -> 
            assertThat(holding.getQuantity()).isGreaterThan(0)
        );
    }
}
