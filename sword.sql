/*
 Navicat Premium Data Transfer

 Source Server         : mysql@localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : sword

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 12/08/2018 22:30:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `chaincode_id` int(11) NOT NULL,
  `key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `modify_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `private_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `public_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `active` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for block
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) NOT NULL,
  `data_hash` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calculated_hash` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `previous_hash` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `envelope_count` int(4) NOT NULL,
  `tx_count` int(5) NOT NULL,
  `r_w_set_count` int(5) NOT NULL,
  `timestamp` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calculate_date` int(8) NOT NULL,
  `create_date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ca
-- ----------------------------
DROP TABLE IF EXISTS `ca`;
CREATE TABLE `ca`  (
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sk_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `certificate_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `flag` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `peer_id` int(9) NOT NULL,
  `tls` int(1) NOT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for chaincode
-- ----------------------------
DROP TABLE IF EXISTS `chaincode`;
CREATE TABLE `chaincode`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `path` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `proposal_wait_time` int(11) NOT NULL DEFAULT 90000,
  `channel_id` int(11) NOT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `source` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `policy` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `chaincode_event_listener` int(1) NOT NULL DEFAULT 0,
  `callback_location` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `events` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `peer_id` int(11) NOT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_listener` int(1) NOT NULL,
  `callback_location` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for league
-- ----------------------------
DROP TABLE IF EXISTS `league`;
CREATE TABLE `league`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orderer
-- ----------------------------
DROP TABLE IF EXISTS `orderer`;
CREATE TABLE `orderer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `location` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `server_crt_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org
-- ----------------------------
DROP TABLE IF EXISTS `org`;
CREATE TABLE `org`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tls` int(11) NOT NULL,
  `msp_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `league_id` int(11) NOT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for peer
-- ----------------------------
DROP TABLE IF EXISTS `peer`;
CREATE TABLE `peer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `location` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `event_hub_location` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `server_crt_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'a', '7545a789ab0bb433e79717c22daf32dd');

SET FOREIGN_KEY_CHECKS = 1;
