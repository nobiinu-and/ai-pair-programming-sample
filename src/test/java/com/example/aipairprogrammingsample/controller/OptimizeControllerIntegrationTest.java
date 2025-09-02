package com.example.aipairprogrammingsample.controller;

import com.example.aipairprogrammingsample.entity.Company;
import com.example.aipairprogrammingsample.entity.CompanyMetal;
import com.example.aipairprogrammingsample.entity.Holding;
import com.example.aipairprogrammingsample.entity.Metal;
import com.example.aipairprogrammingsample.repository.CompanyMetalRepository;
import com.example.aipairprogrammingsample.repository.CompanyRepository;
import com.example.aipairprogrammingsample.repository.HoldingRepository;
import com.example.aipairprogrammingsample.repository.MetalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 最適化API統合テスト
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class OptimizeControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
    private Metal neodymium;
    private Metal dysprosium;

    @BeforeEach
    void setUp() {
        // MockMvcをセットアップ
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // 既存データをクリア
        holdingRepository.deleteAll();
        companyMetalRepository.deleteAll();
        companyRepository.deleteAll();
        metalRepository.deleteAll();

        // テストデータの準備
        setupTestData();
    }

    @Test
    void 正常な最適化リクエストで成功レスポンスが返されること() throws Exception {
        // JSON文字列でリクエストを定義
        String jsonRequest = """
                {
                    "requests": [
                        {
                            "metalCode": "Nd",
                            "requiredQuantity": 500
                        },
                        {
                            "metalCode": "Dy",
                            "requiredQuantity": 200
                        }
                    ]
                }
                """;

        // API呼び出しと検証
        mockMvc.perform(post("/api/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignments").isArray())
                .andExpect(jsonPath("$.assignments[0].metalCode").value("Nd"))
                .andExpect(jsonPath("$.assignments[0].assignedQuantity").value(500))
                .andExpect(jsonPath("$.assignments[1].metalCode").value("Dy"))
                .andExpect(jsonPath("$.assignments[1].assignedQuantity").value(200))
                .andExpect(jsonPath("$.companiesUsed").isArray())
                .andExpect(jsonPath("$.metrics.companyCount").value(1))
                .andExpect(jsonPath("$.metrics.totalAssigned").value(700));
    }

    @Test
    void 複数金属の最適化で早期登録優先が動作すること() throws Exception {
        // JSON文字列でリクエストを定義
        String jsonRequest = """
                {
                    "requests": [
                        {
                            "metalCode": "Nd",
                            "requiredQuantity": 1500
                        },
                        {
                            "metalCode": "Dy",
                            "requiredQuantity": 300
                        }
                    ]
                }
                """;

        // API呼び出しと検証
        mockMvc.perform(post("/api/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignments").isArray())
                .andExpect(jsonPath("$.assignments.length()").value(3)) // 3つの割当
                .andExpect(jsonPath("$.metrics.companyCount").value(2))
                .andExpect(jsonPath("$.metrics.totalAssigned").value(1800));
    }

    @Test
    void 存在しない金属コードでバリデーションエラーが返されること() throws Exception {
        // JSON文字列でリクエストを定義（存在しない金属コード）
        String jsonRequest = """
                {
                    "requests": [
                        {
                            "metalCode": "XX",
                            "requiredQuantity": 100
                        }
                    ]
                }
                """;

        // API呼び出しと検証
        mockMvc.perform(post("/api/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details[0]").value("Metal code not found: XX"));
    }

    @Test
    void 供給量不足で422エラーが返されること() throws Exception {
        // JSON文字列でリクエストを定義（供給量を超える要求）
        String jsonRequest = """
                {
                    "requests": [
                        {
                            "metalCode": "Nd",
                            "requiredQuantity": 2000
                        }
                    ]
                }
                """;

        // API呼び出しと検証
        mockMvc.perform(post("/api/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("InsufficientSupply"))
                .andExpect(jsonPath("$.shortages[0].metalCode").value("Nd"))
                .andExpect(jsonPath("$.shortages[0].missing").value(200));
    }

    @Test
    void 必須フィールド未入力でバリデーションエラーが返されること() throws Exception {
        // JSON文字列でリクエストを定義（空のリクエスト）
        String jsonRequest = """
                {
                    "requests": []
                }
                """;

        // API呼び出しと検証
        mockMvc.perform(post("/api/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    /**
     * テストデータのセットアップ
     */
    private void setupTestData() {
        // 会社データ
        techMetal = companyRepository.save(new Company("TechMetal Corp", "contact@techmetal.com"));
        rareEarth = companyRepository.save(new Company("RareEarth Industries", "info@rareearth.com"));

        // 金属データ
        neodymium = metalRepository.save(new Metal("Nd", "Neodymium", "kg"));
        dysprosium = metalRepository.save(new Metal("Dy", "Dysprosium", "kg"));

        // 会社・金属関連データ
        var techNd = companyMetalRepository.save(new CompanyMetal(techMetal, neodymium));
        var techDy = companyMetalRepository.save(new CompanyMetal(techMetal, dysprosium));
        var rareNd = companyMetalRepository.save(new CompanyMetal(rareEarth, neodymium));

        // 保有量データ（TechMetalが早期登録）
        holdingRepository.save(new Holding(techNd, 1000));   // TechMetal - Nd: 1000kg
        holdingRepository.save(new Holding(techDy, 500));    // TechMetal - Dy: 500kg
        holdingRepository.save(new Holding(rareNd, 800));    // RareEarth - Nd: 800kg
    }
}
