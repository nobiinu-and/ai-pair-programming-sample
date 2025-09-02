package com.example.aipairprogrammingsample.controller;

import com.example.aipairprogrammingsample.dto.ErrorResponse;
import com.example.aipairprogrammingsample.dto.OptimizeRequest;
import com.example.aipairprogrammingsample.dto.OptimizeResponse;
import com.example.aipairprogrammingsample.service.OptimizeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 最適化APIコントローラー
 */
@RestController
@RequestMapping("/api")
public class OptimizeController {

    private static final Logger logger = LoggerFactory.getLogger(OptimizeController.class);

    private final OptimizeService optimizeService;

    public OptimizeController(OptimizeService optimizeService) {
        this.optimizeService = optimizeService;
    }

    /**
     * レアメタル発注最適化API
     * 
     * @param request 最適化リクエスト
     * @param bindingResult バリデーション結果
     * @return 最適化結果またはエラー情報
     */
    @PostMapping("/optimize")
    public ResponseEntity<?> optimize(
            @Valid @RequestBody OptimizeRequest request,
            BindingResult bindingResult) {
        
        logger.info("最適化API呼び出し開始");
        logger.debug("リクエスト内容: {}", request);

        try {
            // バリデーションエラーチェック
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.toList());
                
                logger.warn("バリデーションエラー: {}", errors);
                var errorResponse = new ErrorResponse("Validation failed", errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // 最適化処理実行
            OptimizeResponse response = optimizeService.optimize(request);
            
            logger.info("最適化API呼び出し成功 - 使用会社数: {}, 総割当量: {}", 
                    response.getMetrics().getCompanyCount(), 
                    response.getMetrics().getTotalAssigned());
            
            return ResponseEntity.ok(response);

        } catch (OptimizeService.OptimizationException e) {
            logger.error("最適化処理エラー: {}", e.getMessage());
            
            if (e.isSupplyShortage()) {
                // 供給不足エラー (422 Unprocessable Entity)
                var errorResponse = new ErrorResponse("InsufficientSupply", e.getShortages(), true);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
            } else {
                // バリデーションエラー (400 Bad Request)
                var errorResponse = new ErrorResponse("Validation failed", e.getDetails());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        } catch (Exception e) {
            logger.error("予期しないエラー", e);
            var errorResponse = new ErrorResponse("Internal server error", 
                    List.of("An unexpected error occurred"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
