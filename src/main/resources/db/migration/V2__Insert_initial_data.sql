-- V2__Insert_initial_data.sql
-- テスト用の初期データを挿入

-- 会社データの挿入
INSERT INTO company (id, name, contact, created_at, updated_at) VALUES 
    (RANDOM_UUID(), 'TechMetal Corp', 'contact@techmetal.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (RANDOM_UUID(), 'RareEarth Industries', 'info@rareearth.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (RANDOM_UUID(), 'Global Mining Ltd', 'sales@globalmining.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 金属データの挿入
INSERT INTO metal (id, code, name, unit, created_at, updated_at) VALUES 
    (RANDOM_UUID(), 'Nd', 'Neodymium', 'kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (RANDOM_UUID(), 'Dy', 'Dysprosium', 'kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (RANDOM_UUID(), 'Pr', 'Praseodymium', 'kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (RANDOM_UUID(), 'Tb', 'Terbium', 'kg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 会社・金属関連データの挿入（各会社が扱う金属の関係を定義）
INSERT INTO company_metal (id, company_id, metal_id, created_at, updated_at)
SELECT 
    RANDOM_UUID(),
    c.id,
    m.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM company c
CROSS JOIN metal m
WHERE (c.name = 'TechMetal Corp' AND m.code IN ('Nd', 'Dy'))
   OR (c.name = 'RareEarth Industries' AND m.code IN ('Nd', 'Pr', 'Tb'))
   OR (c.name = 'Global Mining Ltd' AND m.code IN ('Dy', 'Pr'));

-- 保有量データの挿入
INSERT INTO holding (id, company_metal_id, quantity, created_at, updated_at)
SELECT 
    RANDOM_UUID(),
    cm.id,
    CASE 
        WHEN c.name = 'TechMetal Corp' AND m.code = 'Nd' THEN 1000
        WHEN c.name = 'TechMetal Corp' AND m.code = 'Dy' THEN 500
        WHEN c.name = 'RareEarth Industries' AND m.code = 'Nd' THEN 800
        WHEN c.name = 'RareEarth Industries' AND m.code = 'Pr' THEN 600
        WHEN c.name = 'RareEarth Industries' AND m.code = 'Tb' THEN 300
        WHEN c.name = 'Global Mining Ltd' AND m.code = 'Dy' THEN 400
        WHEN c.name = 'Global Mining Ltd' AND m.code = 'Pr' THEN 750
        ELSE 0
    END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM company_metal cm
JOIN company c ON cm.company_id = c.id
JOIN metal m ON cm.metal_id = m.id;
