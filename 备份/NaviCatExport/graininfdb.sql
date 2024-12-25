/*
 Navicat Premium Data Transfer

 Source Server         : GrainInfDB
 Source Server Type    : MySQL
 Source Server Version : 80025 (8.0.25)
 Source Host           : localhost:3306
 Source Schema         : graininfdb

 Target Server Type    : MySQL
 Target Server Version : 80025 (8.0.25)
 File Encoding         : 65001

 Date: 07/12/2024 15:22:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for grain_change_history
-- ----------------------------
DROP TABLE IF EXISTS `grain_change_history`;
CREATE TABLE `grain_change_history`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `grain_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `change_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `change_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `change_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `grain_change_history_ibfk_1`(`grain_id` ASC) USING BTREE,
  CONSTRAINT `grain_change_history_ibfk_1` FOREIGN KEY (`grain_id`) REFERENCES `graindata` (`id_grain`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grain_change_history
-- ----------------------------
INSERT INTO `grain_change_history` VALUES (93, '07dae2f6-61cc-4899-a667-06d7a32d7bfe', '导入更新', '更新粮食信息: 王昊 (编号: 8888888)', '2024-11-30 16:08:37', 'a');
INSERT INTO `grain_change_history` VALUES (94, 'a53b20f7-25b8-42a6-9894-06512df57598', '导入更新', '更新粮食信息: 难绷 (编号: 47774747)', '2024-11-30 16:08:38', 'a');
INSERT INTO `grain_change_history` VALUES (98, 'a53b20f7-25b8-42a6-9894-06512df57598', '删除', '删除粮食: 难绷, 编号: 47774747', '2024-11-30 16:08:54', 'a');
INSERT INTO `grain_change_history` VALUES (100, '07dae2f6-61cc-4899-a667-06d7a32d7bfe', '导入更新', '更新粮食信息: 王昊 (编号: 8888888)', '2024-11-30 16:09:24', 'a');
INSERT INTO `grain_change_history` VALUES (102, '07dae2f6-61cc-4899-a667-06d7a32d7bfe', '导入更新', '更新粮食信息: 王昊 (编号: 8888888)', '2024-11-30 16:10:15', 'a');
INSERT INTO `grain_change_history` VALUES (104, 'a53b20f7-25b8-42a6-9894-06512df57598', '恢复', '恢复已删除的粮食: 难绷', '2024-11-30 16:11:28', 'a');
INSERT INTO `grain_change_history` VALUES (105, 'a53b20f7-25b8-42a6-9894-06512df57598', '删除', '删除粮食: 难绷, 编号: 47774747', '2024-11-30 16:11:38', 'a');
INSERT INTO `grain_change_history` VALUES (106, '07dae2f6-61cc-4899-a667-06d7a32d7bfe', '导入更新', '更新粮食信息: 王昊 (编号: 8888888)', '2024-11-30 16:11:46', 'a');
INSERT INTO `grain_change_history` VALUES (109, '9f77f104-6012-4c26-98b0-3724bc2e6dfb', '删除', '删除粮食: 鸣潮, 编号: 698', '2024-11-30 16:17:10', 'a');
INSERT INTO `grain_change_history` VALUES (111, '07dae2f6-61cc-4899-a667-06d7a32d7bfe', '导入更新', '更新粮食信息: 王昊 (编号: 8888888)', '2024-11-30 16:17:33', 'a');
INSERT INTO `grain_change_history` VALUES (113, '07dae2f6-61cc-4899-a667-06d7a32d7bfe', '导出', '导出2条粮食信息到文件: C:\\Users\\awsl1\\Desktop\\粮食信息.xlsx', '2024-11-30 16:43:54', 'a');
INSERT INTO `grain_change_history` VALUES (114, '5598f4a6-ddff-44b5-9560-270186c57aac', '添加', '添加新粮食: 梁梓祺, 编号: 266, 类型: 6667, 价格: 75824782.00', '2024-12-04 15:41:11', 'a');
INSERT INTO `grain_change_history` VALUES (115, '5598f4a6-ddff-44b5-9560-270186c57aac', '修改', '修改粮食信息: 梁梓祺, 编号: 266, 类型: 666667, 价格: 75824782.00', '2024-12-04 15:41:22', 'a');

-- ----------------------------
-- Table structure for graindata
-- ----------------------------
DROP TABLE IF EXISTS `graindata`;
CREATE TABLE `graindata`  (
  `id_grain` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `grain_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `grain_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `grain_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `grain_price` double NOT NULL,
  `grainShelfLife` double NOT NULL,
  `grain_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id_grain`) USING BTREE,
  UNIQUE INDEX `grain_code`(`grain_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of graindata
-- ----------------------------
INSERT INTO `graindata` VALUES ('07dae2f6-61cc-4899-a667-06d7a32d7bfe', '8888888', '王昊', '舍长', 6666666, 872527858725, '这个王昊啊，一天不吃的也是神人了', '2024-11-30 16:01:32', '2024-11-30 16:01:32', 0);
INSERT INTO `graindata` VALUES ('5598f4a6-ddff-44b5-9560-270186c57aac', '266', '梁梓祺', '666667', 75824782, 0.11111111, '皓齿', '2024-12-04 15:41:11', '2024-12-04 15:41:22', 0);
INSERT INTO `graindata` VALUES ('9f77f104-6012-4c26-98b0-3724bc2e6dfb', '698', '鸣潮', '难绷', 1, 66666666, '王昊也干了', '2024-11-30 16:11:46', '2024-11-30 16:17:10', 1);
INSERT INTO `graindata` VALUES ('a0a2f036-96cc-4629-8a8e-2d3feda4bf22', '111474', '高见', '铸币', 0.0002, 0.001, '难绷', '2024-11-30 16:17:33', '2024-11-30 16:17:33', 0);
INSERT INTO `graindata` VALUES ('a53b20f7-25b8-42a6-9894-06512df57598', '47774747', '难绷', '啊？哈？', 63636, 8, '测试', '2024-11-30 16:03:09', '2024-11-30 16:11:39', 1);

-- ----------------------------
-- Table structure for system_logs
-- ----------------------------
DROP TABLE IF EXISTS `system_logs`;
CREATE TABLE `system_logs`  (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `log_time` datetime NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_logs
-- ----------------------------
INSERT INTO `system_logs` VALUES (1, 'a', '修改用户信息', '系统管理员a更改为系统管理员aaa', '2024-11-28 17:29:14');
INSERT INTO `system_logs` VALUES (2, 'aaa', '注销', '系统管理员aaa注销了账号', '2024-11-28 17:29:28');
INSERT INTO `system_logs` VALUES (3, 'a', '注册', '注册为系统管理员', '2024-11-28 17:29:39');
INSERT INTO `system_logs` VALUES (5, 'a', '注册', '注册为系统管理员', '2024-11-30 16:32:49');
INSERT INTO `system_logs` VALUES (6, 'a', '导出粮食信息', '导出2条粮食信息到Excel文件: C:\\Users\\awsl1\\Desktop\\粮食信息.xlsx', '2024-12-03 21:36:42');

-- ----------------------------
-- Table structure for userdata
-- ----------------------------
DROP TABLE IF EXISTS `userdata`;
CREATE TABLE `userdata`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userdata
-- ----------------------------
INSERT INTO `userdata` VALUES (1, 'ErgouTree', 'zjm10086', '系统管理员', '2024-11-17 15:51:59', '2024-11-17 15:51:59');
INSERT INTO `userdata` VALUES (2, 'BBbird', 'lpf666666', '巡检员', '2024-11-17 15:56:41', '2024-11-17 15:56:41');
INSERT INTO `userdata` VALUES (3, 'admin', 'password', '系统管理员', '2024-11-17 19:14:14', '2024-11-17 19:14:14');
INSERT INTO `userdata` VALUES (4, 'LiLiang', 'liliang', '仓库管理员', '2024-11-18 08:13:39', '2024-11-18 08:13:39');
INSERT INTO `userdata` VALUES (6, 'ad2', '666666', '巡检员', '2024-11-20 16:49:19', '2024-11-20 16:49:19');
INSERT INTO `userdata` VALUES (7, 'adp', '666666', '巡检员', '2024-11-25 11:37:37', '2024-11-25 11:37:37');
INSERT INTO `userdata` VALUES (8, 'testUser', 'newPassword', '普通用户', '2024-11-27 15:53:39', '2024-11-27 15:53:39');
INSERT INTO `userdata` VALUES (18, 'a', 'aaaaaa', '系统管理员', '2024-11-30 16:32:49', '2024-11-30 16:32:49');

SET FOREIGN_KEY_CHECKS = 1;
