# 生成AIとのペアプロ実践記録

## 1. 概要
- 短い説明: 生成AIとのペアプロ実践を記録するためのサンプルプロジェクト。  例題として「レアメタルの発注最適化」を行う

## 2. 目的・ゴール
- 主要ゴール:
  - 開発ワークフロー比較（vibecoding / vibecoding-tdd）の記録と検証
  - レアメタル発注の基本的な最適化機能を提供
  - 教育・デモ用に再現可能なコードベースと CI を用意

## 3. ターゲットユーザー・利用シナリオ
- ユーザー: サプライチェーン担当者、エンジニア、教育目的の受講者
- 利用フロー: 販売側のレアメタル保有量を登録 -> 要求に応じた発注最適解を提示

## 4. プラットフォーム
- 対応環境: サーバーサイド REST API（Docker でのコンテナ化を想定）
- 開発環境: devcontainer（Ubuntu 24.04.2 LTS）

## 5. 主要機能（優先度付き）
0. レアメタルごとに早く登録した会社を優先する提案 (ひな型部分)
1. レアメタルごとに発注会社数を最小化する提案
2. レアメタルごとに情報の鮮度が高い発注会社を優先的して、発注会社数を最小化する提案
3. レアメタルごとに情報の鮮度が高く、評価も高い発注会社を優先的して、発注会社数を最小化する提案
4. 全体(=複数のレアメタルを想定)として発注会社数を最小化する提案
5. 全体(=複数のレアメタルを想定)として情報の鮮度が高い発注会社を優先的して、発注会社数を最小化する提案
6. [重要] Flyway による DB マイグレーション + JPA エンティティ

mainブランチは主要機能0に関する実装とCIのワークフローが定義されている

## 6. 非機能要件
- パフォーマンス: 単一最適化リクエスト応答 < 2s（MVP 目安）
- 可用性: 開発段階では単一インスタンス
- スケーラビリティ: 考慮しない
- セキュリティ: TLS 想定、認証なし
- ロギング・監視: SLF4J + Logback

## 7. データモデル / API

  このセクションは要件どおり「会社 (Company)」「金属 (Metal)」「保有量 (Holding)」の 3 テーブル構成に限定し、API は発注最適化 (`POST /api/optimize`) のみを定義する。

### 7.1 データモデル（関連テーブルを使用した設計）

#### 基本エンティティ
- Company
  - `id`: UUID (PK)
  - `name`: string (必須)
  - `contact`: string (任意)
  - `created_at`: timestamp (挿入時に設定)
  - `updated_at`: timestamp (更新時に設定)

- Metal
  - `id`: UUID (PK)
  - `code`: string (例: "Nd", "Dy" — 一意)
  - `name`: string (例: "Neodymium")
  - `unit`: string (例: "kg", 任意)
  - `created_at`: timestamp (挿入時に設定)
  - `updated_at`: timestamp (更新時に設定)

#### 関連テーブル
- CompanyMetal （Company と Metal の多対多関係）
  - `id`: UUID (PK)
  - `company_id`: UUID (FK -> Company.id)
  - `metal_id`: UUID (FK -> Metal.id)
  - `created_at`: timestamp (挿入時に設定)
  - `updated_at`: timestamp (更新時に設定)
  - 複合ユニーク制約: (company_id, metal_id)

- Holding （保有量の管理）
  - `id`: UUID (PK)
  - `company_metal_id`: UUID (FK -> CompanyMetal.id)
  - `quantity`: integer (非負、単位は `Metal.unit` に従う)
  - `created_at`: timestamp (挿入時に設定)
  - `updated_at`: timestamp (更新時に設定)

設計メモ:
- 関連テーブル `CompanyMetal` を使用して Company と Metal の関係を明示的に管理
- `Holding` は `CompanyMetal` を参照することで、会社と金属の組み合わせごとの保有量を管理
- `quantity` は整数で扱う（要件）。負数は許容しない。
- インデックス: `company_metals(company_id)`, `company_metals(metal_id)`, `holdings(company_metal_id)` を推奨
- Flyway V1 には上記 4 テーブルの CREATE TABLE を含める  

### 7.2 API: POST /api/optimize (最適化 API のみ)

この API はクライアントが要求する各金属の必要量に対し、最小ヒューリスティック（機能0: 早く登録した会社を優先）で割当を返す。

  エンドポイント
  - POST /api/optimize

  リクエスト JSON スキーマ
  {
    "requests": [
      { "metalCode": "Nd", "requiredQuantity": 500 },
      { "metalCode": "Dy", "requiredQuantity": 200 }
    ]
  }

  バリデーション
  - `requests` は空であってはならない
  - `metalCode` は登録済みの `Metal.code` であること（存在チェック）
  - `requiredQuantity` は整数かつ >= 1

  エラーレスポンス
  - 400 Bad Request
    - body: { "error": "詳細メッセージ", "details": [...] }
    - ケース: 不正な JSON、必須フィールド欠落、数値範囲外
  - 422 Unprocessable Entity
    - body: { "error": "InsufficientSupply", "shortages": [ { "metalCode":"Nd", "missing": 100 } ] }
    - ケース: 要求量が合計保有量を超える場合（不足があるとき）

  戻り値（成功 200 OK）レスポンス例
  {
    "assignments": [
      { "metalCode": "Nd", "companyId": "c1-uuid", "assignedQuantity": 500 },
      { "metalCode": "Dy", "companyId": "c1-uuid", "assignedQuantity": 200 }
    ],
    "companiesUsed": [ "c1-uuid" ],
    "metrics": { "companyCount": 1, "totalAssigned": 700 }
  }

## 8. 技術スタックと制約
- 言語 / フレームワーク: Java 17+, Spring Boot
- ビルド: Maven
- 永続化: JPA（Hibernate）
- マイグレーション: Flyway
- テスト: JUnit5, Mockito（TDD ブランチでは Testcontainers 推奨）
- CI: GitHub Actions（`mvn -B clean verify`）
- コンテナ: Dockerfile / docker-compose（開発時オプション）

---
作成者: 仕様テンプレート自動生成
