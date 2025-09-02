-- V1__Create_initial_schema.sql
-- レアメタル発注最適化システムの初期スキーマ

-- 会社テーブル
CREATE TABLE company (
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 金属テーブル
CREATE TABLE metal (
    id UUID NOT NULL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 会社・金属関連テーブル（多対多の関係）
CREATE TABLE company_metal (
    id UUID NOT NULL PRIMARY KEY,
    company_id UUID NOT NULL,
    metal_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_company_metal_company FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT fk_company_metal_metal FOREIGN KEY (metal_id) REFERENCES metal(id),
    CONSTRAINT uk_company_metal_company_metal UNIQUE (company_id, metal_id)
);

-- 保有量テーブル
CREATE TABLE holding (
    id UUID NOT NULL PRIMARY KEY,
    company_metal_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_holding_company_metal FOREIGN KEY (company_metal_id) REFERENCES company_metal(id)
);

-- インデックス作成（パフォーマンス向上のため）
CREATE INDEX idx_company_metal_company_id ON company_metal(company_id);
CREATE INDEX idx_company_metal_metal_id ON company_metal(metal_id);
CREATE INDEX idx_holding_company_metal_id ON holding(company_metal_id);

-- コメント追加
COMMENT ON TABLE company IS '会社情報';
COMMENT ON TABLE metal IS '金属情報';
COMMENT ON TABLE company_metal IS '会社と金属の関連テーブル';
COMMENT ON TABLE holding IS '保有量情報';

COMMENT ON COLUMN company.id IS 'UUID主キー';
COMMENT ON COLUMN company.name IS '会社名（必須）';
COMMENT ON COLUMN company.contact IS '連絡先（任意）';

COMMENT ON COLUMN metal.id IS 'UUID主キー';
COMMENT ON COLUMN metal.code IS '金属コード（例: Nd, Dy）';
COMMENT ON COLUMN metal.name IS '金属名（例: Neodymium）';
COMMENT ON COLUMN metal.unit IS '単位（例: kg）';

COMMENT ON COLUMN company_metal.company_id IS '会社ID（外部キー）';
COMMENT ON COLUMN company_metal.metal_id IS '金属ID（外部キー）';

COMMENT ON COLUMN holding.company_metal_id IS '会社・金属関連ID（外部キー）';
COMMENT ON COLUMN holding.quantity IS '保有量（非負整数）';
