/*
 Navicat Premium Data Transfer

 Source Server         : RuoYiCloud
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : ry-cloud

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 23/08/2023 14:31:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`user_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和部门关联表';

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (1, 103);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (1, 105);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (1, 200);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (2, 105);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (100, 200);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (101, 200);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (102, 106);
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (103, 106);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
