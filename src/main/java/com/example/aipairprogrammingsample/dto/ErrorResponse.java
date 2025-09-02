package com.example.aipairprogrammingsample.dto;

import java.util.List;

/**
 * エラーレスポンスDTO
 */
public class ErrorResponse {

    private String error;
    private List<String> details;
    private List<Shortage> shortages;

    // デフォルトコンストラクタ
    public ErrorResponse() {}

    // 一般的なエラー用コンストラクタ
    public ErrorResponse(String error, List<String> details) {
        this.error = error;
        this.details = details;
    }

    // 供給不足エラー用コンストラクタ
    public ErrorResponse(String error, List<Shortage> shortages, boolean isShortage) {
        this.error = error;
        this.shortages = shortages;
    }

    // Getters and Setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public List<Shortage> getShortages() {
        return shortages;
    }

    public void setShortages(List<Shortage> shortages) {
        this.shortages = shortages;
    }

    /**
     * 不足情報
     */
    public static class Shortage {
        private String metalCode;
        private Integer missing;

        // デフォルトコンストラクタ
        public Shortage() {}

        // コンストラクタ
        public Shortage(String metalCode, Integer missing) {
            this.metalCode = metalCode;
            this.missing = missing;
        }

        // Getters and Setters
        public String getMetalCode() {
            return metalCode;
        }

        public void setMetalCode(String metalCode) {
            this.metalCode = metalCode;
        }

        public Integer getMissing() {
            return missing;
        }

        public void setMissing(Integer missing) {
            this.missing = missing;
        }
    }
}
