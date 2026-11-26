-- =============================================
-- AI 错题本 - 数据库初始化脚本
-- 执行前请确保 MySQL 已启动
-- =============================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS mistake_book
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE mistake_book;

-- 2. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码（MD5加密）',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB COMMENT='用户表';

-- 3. 错题表（核心表）
DROP TABLE IF EXISTS `mistake`;
CREATE TABLE `mistake` (
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`          BIGINT        NOT NULL COMMENT '所属用户ID',
    `title`            TEXT          NOT NULL COMMENT '题目内容',
    `my_answer`        TEXT          DEFAULT NULL COMMENT '我的答案',
    `correct_answer`   TEXT          DEFAULT NULL COMMENT '正确答案',
    `analysis`         TEXT          DEFAULT NULL COMMENT '题目解析（AI生成）',
    `subject`          VARCHAR(50)   DEFAULT NULL COMMENT '学科（数学/物理/语文等）',
    `knowledge_point`  VARCHAR(100)  DEFAULT NULL COMMENT '知识点标签',
    `status`           TINYINT       DEFAULT 0 COMMENT '状态：0-未复习 1-已复习',
    `create_time`      DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_subject` (`subject`)
) ENGINE=InnoDB COMMENT='错题表';

-- =============================================
-- 插入一条测试用户（密码是 123456 的 MD5：e10adc3949ba59abbe56e057f20f883e）
-- =============================================
INSERT INTO `user` (`username`, `password`, `nickname`) VALUES ('test', 'e10adc3949ba59abbe56e057f20f883e', '测试用户');

-- 插入几条测试错题
INSERT INTO `mistake` (`user_id`, `title`, `my_answer`, `correct_answer`, `analysis`, `subject`, `knowledge_point`) VALUES
(1, '已知函数 f(x) = x² + 2x - 3，求 f(2) 的值', '3', '5', 'f(2) = 2² + 2×2 - 3 = 4 + 4 - 3 = 5', '数学', '二次函数'),
(1, '下列哪个是 Java 的基本数据类型？A. String  B. Integer  C. int  D. Double', 'B', 'C', 'int 是基本数据类型，Integer 是其包装类', '计算机', 'Java基础'),
(1, '1 + 1 = ?', '1', '2', '这是基础加法运算', '数学', '算术');
