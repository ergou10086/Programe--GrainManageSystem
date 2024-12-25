-- 创建数据库
CREATE DATABASE IF NOT EXISTS graininfdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用数据库
USE graininfdb;


-- 创建用户表
CREATE TABLE IF NOT EXISTS userdata (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT * FROM userdata;


-- 创建粮食表
CREATE TABLE graindata (
    id_grain VARCHAR(36) PRIMARY KEY,     -- 粮食ID，使用UUID
    grain_code VARCHAR(50) UNIQUE,   -- 粮食编码,唯一
    grain_name VARCHAR(50) NOT NULL,      -- 粮食名称
    grain_type VARCHAR(50) NOT NULL,    -- 粮食种类
    grain_price DECIMAL(10, 2) NOT NULL,   -- 粮食价格
    grain_remark TEXT,                    -- 备注信息
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
    is_deleted TINYINT(1) DEFAULT 0   -- 软删除标记，0表示未删除，1表示已删除
);
-- 添加字段保存日期
ALTER TABLE graindata ADD grainShelfLife DECIMAL(5, 2) NOT NULL DEFAULT 0.00;
SELECT * FROM graindata;   -- 查看表结构
ALTER TABLE graindata MODIFY grainShelfLife DECIMAL(5, 2) NOT NULL DEFAULT 0.00 AFTER grain_price;
-- GRANT ALL PRIVILEGES ON *.* TO 'Liliang'@'10.7.85.98' IDENTIFIED BY 'zjm10086' WITH GRANT OPTION;

-- 创建用户李亮
CREATE USER 'Liliang'@'10.7.85.246' IDENTIFIED BY 'zjm10086';
GRANT ALL PRIVILEGES ON *.* TO 'Liliang'@'10.7.85.246' WITH GRANT OPTION;
flush privileges;

-- 创建用户王昊
CREATE USER 'Wanghao'@'10.7.85.10' IDENTIFIED BY 'zjm10086';
GRANT ALL PRIVILEGES ON *.* TO 'Wanghao'@'10.7.85.10' WITH GRANT OPTION;
flush privileges;


-- 创建用户景硕
CREATE USER 'Jingsuo'@'10.7.85.185' IDENTIFIED BY 'zjm10086';
GRANT ALL PRIVILEGES ON *.* TO 'Jingsuo'@'10.7.85.185' WITH GRANT OPTION;
flush privileges;

SHOW GRANTS FOR 'Liliang'@'10.7.85.246';



-- 创建变更历史表
CREATE TABLE grain_change_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    grain_id VARCHAR(36),                 -- 关联的粮食ID
    change_type VARCHAR(20),              -- 变更类型（出库/入库/售完/补充）
    change_detail TEXT,                   -- 变更详情
    change_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 变更时间
    operator VARCHAR(50),                 -- 操作人
    FOREIGN KEY (grain_id) REFERENCES graindata(id_grain)  -- 外键约束，表中的 grain_id 字段的值必须是来自于 graindata 表中
);

SELECT * FROM grain_change_history;