package com.example.aipairprogrammingsample.service;

import com.example.aipairprogrammingsample.dto.ErrorResponse;
import com.example.aipairprogrammingsample.dto.OptimizeRequest;
import com.example.aipairprogrammingsample.dto.OptimizeResponse;
import com.example.aipairprogrammingsample.entity.Holding;
import com.example.aipairprogrammingsample.repository.HoldingRepository;
import com.example.aipairprogrammingsample.repository.MetalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * レアメタル発注最適化サービス
 * アルゴリズム: 金属ごとに早く登録した会社を優先する（機能0）
 */
@Service
public class OptimizeService {

    private static final Logger logger = LoggerFactory.getLogger(OptimizeService.class);

    private final MetalRepository metalRepository;
    private final HoldingRepository holdingRepository;

    public OptimizeService(MetalRepository metalRepository, HoldingRepository holdingRepository) {
        this.metalRepository = metalRepository;
        this.holdingRepository = holdingRepository;
    }

    /**
     * 最適化処理を実行
     * @param request 最適化リクエスト
     * @return 最適化結果
     * @throws OptimizationException バリデーションエラーまたは供給不足エラー
     */
    public OptimizeResponse optimize(OptimizeRequest request) throws OptimizationException {
        logger.debug("開始: 最適化処理 - リクエスト: {}", request);

        // バリデーション
        validateRequest(request);

        // 金属コードごとの割り当て処理
        List<OptimizeResponse.Assignment> assignments = new ArrayList<>();
        Set<UUID> companiesUsed = new HashSet<>();
        int totalAssigned = 0;

        for (var metalRequest : request.getRequests()) {
            logger.debug("金属 {} の最適化開始 - 必要量: {}", 
                    metalRequest.getMetalCode(), metalRequest.getRequiredQuantity());

            var metalAssignments = optimizeForMetal(
                    metalRequest.getMetalCode(), 
                    metalRequest.getRequiredQuantity()
            );

            assignments.addAll(metalAssignments);

            // 使用された会社と総割当量を集計
            for (var assignment : metalAssignments) {
                companiesUsed.add(assignment.getCompanyId());
                totalAssigned += assignment.getAssignedQuantity();
            }
        }

        // レスポンス作成
        var metrics = new OptimizeResponse.Metrics(companiesUsed.size(), totalAssigned);
        var response = new OptimizeResponse(assignments, new ArrayList<>(companiesUsed), metrics);

        logger.debug("完了: 最適化処理 - 使用会社数: {}, 総割当量: {}", 
                companiesUsed.size(), totalAssigned);

        return response;
    }

    /**
     * リクエストのバリデーション
     */
    private void validateRequest(OptimizeRequest request) throws OptimizationException {
        List<String> errors = new ArrayList<>();

        for (var metalRequest : request.getRequests()) {
            // 金属コードの存在チェック
            if (!metalRepository.existsByCode(metalRequest.getMetalCode())) {
                errors.add("Metal code not found: " + metalRequest.getMetalCode());
            }
        }

        if (!errors.isEmpty()) {
            throw new OptimizationException("Validation failed", errors);
        }
    }

    /**
     * 指定された金属の最適化処理（早期登録優先アルゴリズム）
     */
    private List<OptimizeResponse.Assignment> optimizeForMetal(String metalCode, Integer requiredQuantity) 
            throws OptimizationException {
        
        // 該当金属の保有量を登録日時順（早期登録優先）で取得
        List<Holding> holdings = holdingRepository
                .findByMetalCodeWithPositiveQuantityOrderByCreatedAtAsc(metalCode);

        logger.debug("金属 {} の保有データ数: {}", metalCode, holdings.size());

        // 総保有量チェック
        int totalAvailable = holdings.stream()
                .mapToInt(Holding::getQuantity)
                .sum();

        if (totalAvailable < requiredQuantity) {
            var shortage = new ErrorResponse.Shortage(metalCode, requiredQuantity - totalAvailable);
            throw new OptimizationException("InsufficientSupply", List.of(shortage), true);
        }

        // 早期登録順で割り当て
        List<OptimizeResponse.Assignment> assignments = new ArrayList<>();
        int remainingQuantity = requiredQuantity;

        for (Holding holding : holdings) {
            if (remainingQuantity <= 0) {
                break;
            }

            UUID companyId = holding.getCompanyMetal().getCompany().getId();
            int assignedQuantity = Math.min(holding.getQuantity(), remainingQuantity);

            assignments.add(new OptimizeResponse.Assignment(metalCode, companyId, assignedQuantity));
            remainingQuantity -= assignedQuantity;

            logger.debug("割り当て: 金属={}, 会社ID={}, 割当量={}, 残り必要量={}", 
                    metalCode, companyId, assignedQuantity, remainingQuantity);
        }

        return assignments;
    }

    /**
     * 最適化処理の例外クラス
     */
    public static class OptimizationException extends Exception {
        private final List<String> details;
        private final List<ErrorResponse.Shortage> shortages;

        // バリデーションエラー用
        public OptimizationException(String message, List<String> details) {
            super(message);
            this.details = details;
            this.shortages = null;
        }

        // 供給不足エラー用
        public OptimizationException(String message, List<ErrorResponse.Shortage> shortages, boolean isShortage) {
            super(message);
            this.details = null;
            this.shortages = shortages;
        }

        public List<String> getDetails() {
            return details;
        }

        public List<ErrorResponse.Shortage> getShortages() {
            return shortages;
        }

        public boolean isSupplyShortage() {
            return shortages != null;
        }
    }
}
