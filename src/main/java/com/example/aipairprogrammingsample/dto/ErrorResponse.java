package com.example.aipairprogrammingsample.dto;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * エラーレスポンスDTO
 */
@Setter
@Getter
public class ErrorResponse {

    private String error;
    private List<String> details;
    private List<Shortage> shortages;

    // 一般的なエラー用コンストラクタ
    public ErrorResponse(String error, List<String> details) {
        this.error = error;
        if (details == null) {
            this.details = new ArrayList<>();
        } else {
            this.details = new ArrayList<>(details);
        }
        this.shortages = new ArrayList<>();
    }    

    // 供給不足エラー用コンストラクタ
    public ErrorResponse(String error, List<Shortage> shortages, boolean isShortage) {
        this.error = error;
        if (shortages == null) {
            this.shortages = new ArrayList<>();
        } else {
            this.shortages = new ArrayList<>(shortages);
        }
        this.details = new ArrayList<>();
    }

    public ErrorResponse() {
        this.details = new ArrayList<>();
        this.shortages = new ArrayList<>();
    }

    public List<String> getDetails() {
        return new ArrayList<>(details);
    }

    public void setDetails(List<String> details) {
        this.details = new ArrayList<>(details);
    }

    public List<Shortage> getShortages() {
        return new ArrayList<>(shortages);
    }

    public void setShortages(List<Shortage> shortages) {
        this.shortages = new ArrayList<>(shortages);
    }

    /**
     * 不足情報
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Shortage {
        private String metalCode;
        private Integer missing;
    }
}
