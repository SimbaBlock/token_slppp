/*
Navicat MySQL Data Transfer

Source Server         : top7 区块浏览器
Source Server Version : 50729
Source Host           : 47.110.137.123:3306
Source Database       : slppp

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-04-16 20:11:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for code_dbinfo
-- ----------------------------
DROP TABLE IF EXISTS `code_dbinfo`;
CREATE TABLE `code_dbinfo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '别名',
  `db_driver` varchar(100) NOT NULL COMMENT '数据库驱动',
  `db_url` varchar(200) NOT NULL COMMENT '数据库地址',
  `db_user_name` varchar(100) NOT NULL COMMENT '数据库账户',
  `db_password` varchar(100) NOT NULL COMMENT '连接密码',
  `db_type` varchar(10) DEFAULT NULL COMMENT '数据库类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='数据库链接信息';

-- ----------------------------
-- Records of code_dbinfo
-- ----------------------------

-- ----------------------------
-- Table structure for genesis_address
-- ----------------------------
DROP TABLE IF EXISTS `genesis_address`;
CREATE TABLE `genesis_address` (
  `txid` varchar(255) NOT NULL,
  `issue_address` varchar(255) DEFAULT NULL,
  `issue_vout` int(11) DEFAULT NULL,
  `raise_address` varchar(255) DEFAULT NULL,
  `raise_vout` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of genesis_address
-- ----------------------------
INSERT INTO `genesis_address` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '17vVkTTvyMGjSma1wyTdyofn5iQu6SbHXn', '1', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', '2');

-- ----------------------------
-- Table structure for kyc_address
-- ----------------------------
DROP TABLE IF EXISTS `kyc_address`;
CREATE TABLE `kyc_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `id_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kyc_address
-- ----------------------------
INSERT INTO `kyc_address` VALUES ('2', '1KrQoqUXY93cSAegJqSm2zSTuM3CQ64unX', 'fgh', 'sdfsd');
INSERT INTO `kyc_address` VALUES ('3', '1HmiHiDqBuumMXpGYdVAWvgi21DzQV91C8', 'sdf', 'fdg');
INSERT INTO `kyc_address` VALUES ('4', '1LQ5kvQUoE1iat3aVkjRkmj6nwvntkgz1m', 'wer', 'asd');
INSERT INTO `kyc_address` VALUES ('5', '19VvkL2jZctXr7NQEWwuSyXMAWGzMCzFba', 'qw', 'wqe');
INSERT INTO `kyc_address` VALUES ('6', '1MUMotCC19QXiz6Tyk3kvMC756DDrH3q2w', 'zxc', 'ret');
INSERT INTO `kyc_address` VALUES ('7', '1ApUhRDmUKi2oHkS9K9nXQ36uwnPXz1Hei', 'dsf', 'xc');
INSERT INTO `kyc_address` VALUES ('8', '1JYGCbRCFbh9xmDVyzMPF2g2rBdwe9No5c', 'AS', 'fdgs');
INSERT INTO `kyc_address` VALUES ('9', '18RnWjAjSjYwVC7juVypnsUAmvNGMJ72vH', '利斯海', '321312312312312312312312312');
INSERT INTO `kyc_address` VALUES ('10', '1J9rMH3XErLYjWcebs9KhNs1tMVLS1hivA', 'ACAI', '321312312312312312312312312');
INSERT INTO `kyc_address` VALUES ('11', '1AXB3eyoJt66Qkd2Jb69jxwW6yrjoCmQmK', 'ACAI', '321312312312312312312312312');
INSERT INTO `kyc_address` VALUES ('12', '1AXB3eyoJt66Qkd2Jb69jxwW6yrjoCmQmKi', null, null);
INSERT INTO `kyc_address` VALUES ('13', '1NSY3HXkj1UajvZd2zfRjaZvQZvy8fQBWD', 'ACAI3', '321312312312312312312312312');
INSERT INTO `kyc_address` VALUES ('14', '1F2LhV1aHuekX7EFYUYA36EXf7fQLJp3f5', 'ACAI3', '321312312312312312312312312');
INSERT INTO `kyc_address` VALUES ('15', '17vVkTTvyMGjSma1wyTdyofn5iQu6SbHXn', 'nana', '1234567989');
INSERT INTO `kyc_address` VALUES ('16', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', 'wewqr', '1234567989');
INSERT INTO `kyc_address` VALUES ('17', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', 'yuiibd', '1234567989');

-- ----------------------------
-- Table structure for send_raw_type
-- ----------------------------
DROP TABLE IF EXISTS `send_raw_type`;
CREATE TABLE `send_raw_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(32) DEFAULT NULL,
  `txid` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of send_raw_type
-- ----------------------------
INSERT INTO `send_raw_type` VALUES ('38', 'issue', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463');

-- ----------------------------
-- Table structure for send_vin
-- ----------------------------
DROP TABLE IF EXISTS `send_vin`;
CREATE TABLE `send_vin` (
  `txid` varchar(128) DEFAULT NULL,
  `vinvout` int(11) DEFAULT NULL,
  `vintxid` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of send_vin
-- ----------------------------

-- ----------------------------
-- Table structure for send_vout
-- ----------------------------
DROP TABLE IF EXISTS `send_vout`;
CREATE TABLE `send_vout` (
  `token_id` varchar(128) DEFAULT NULL,
  `txid` varchar(128) DEFAULT NULL,
  `n` int(11) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of send_vout
-- ----------------------------

-- ----------------------------
-- Table structure for slp
-- ----------------------------
DROP TABLE IF EXISTS `slp`;
CREATE TABLE `slp` (
  `txid` varchar(128) DEFAULT NULL,
  `transaction_type` varchar(32) DEFAULT NULL,
  `token_ticker` varchar(32) DEFAULT NULL,
  `token_name` varchar(255) DEFAULT NULL,
  `token_document_url` varchar(1024) DEFAULT NULL,
  `token_document_hash` varchar(255) DEFAULT NULL,
  `token_decimal` int(11) DEFAULT NULL,
  `mint_baton_vout` int(11) DEFAULT NULL,
  `initial_token_mint_quantity` varchar(128) DEFAULT NULL,
  `original_address` varchar(128) DEFAULT NULL,
  `init_issue_address` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of slp
-- ----------------------------
INSERT INTO `slp` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', 'GENESIS', 'yuio', 'youafe', 'asfjeimndew', '122dfqwcq', '8', '2', '1000000000', '17vVkTTvyMGjSma1wyTdyofn5iQu6SbHXn', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');

-- ----------------------------
-- Table structure for slp_mint
-- ----------------------------
DROP TABLE IF EXISTS `slp_mint`;
CREATE TABLE `slp_mint` (
  `transaction_type` varchar(255) DEFAULT NULL,
  `token_id` varchar(512) DEFAULT NULL,
  `mint_baton_vout` int(11) DEFAULT NULL,
  `additional_token_quantity` varchar(1024) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `minter_address` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of slp_mint
-- ----------------------------
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', null, '0');
INSERT INTO `slp_mint` VALUES ('mint', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '100000000000', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', null, '0');

-- ----------------------------
-- Table structure for slp_send
-- ----------------------------
DROP TABLE IF EXISTS `slp_send`;
CREATE TABLE `slp_send` (
  `txid` varchar(128) DEFAULT NULL,
  `token_id` varchar(128) DEFAULT NULL,
  `token_output_quantity` varchar(1024) DEFAULT NULL,
  `vout` int(11) DEFAULT NULL,
  `precition` int(11) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of slp_send
-- ----------------------------
INSERT INTO `slp_send` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '100000', '0', '8', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', '0');

-- ----------------------------
-- Table structure for slp_send_quantity
-- ----------------------------
DROP TABLE IF EXISTS `slp_send_quantity`;
CREATE TABLE `slp_send_quantity` (
  `txid` varchar(128) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `token_quantity` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of slp_send_quantity
-- ----------------------------

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(32) DEFAULT NULL,
  `hex` varchar(32) DEFAULT NULL,
  `source_address` varchar(128) DEFAULT NULL,
  `receiving_address` varchar(128) DEFAULT NULL,
  `token` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of token
-- ----------------------------

-- ----------------------------
-- Table structure for token_assets
-- ----------------------------
DROP TABLE IF EXISTS `token_assets`;
CREATE TABLE `token_assets` (
  `token_id` varchar(128) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `token` decimal(32,8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of token_assets
-- ----------------------------
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '17vVkTTvyMGjSma1wyTdyofn5iQu6SbHXn', '1000000000.00000000');
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', '100000000000.00000000');
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', '100000000000.00000000');
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', '100000000000.00000000');
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', '100000000000.00000000');
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc', '100000.00000000');
INSERT INTO `token_assets` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d', '-100000.00000000');

-- ----------------------------
-- Table structure for transcation
-- ----------------------------
DROP TABLE IF EXISTS `transcation`;
CREATE TABLE `transcation` (
  `txid` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of transcation
-- ----------------------------
INSERT INTO `transcation` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463');
INSERT INTO `transcation` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355');
INSERT INTO `transcation` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611');
INSERT INTO `transcation` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280');
INSERT INTO `transcation` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c');
INSERT INTO `transcation` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088');

-- ----------------------------
-- Table structure for vin
-- ----------------------------
DROP TABLE IF EXISTS `vin`;
CREATE TABLE `vin` (
  `txid` varchar(128) DEFAULT NULL,
  `vin_txid` varchar(128) DEFAULT NULL,
  `vout` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vin
-- ----------------------------
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '151d76099c75a7c42a26a763b072665b47d73b1c37862c4974d668936e790887', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '4e5ecc1d1ea3829bbde30bfa1d6aa84b6265af2782d5e383cc9b3744cde0202d', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '5c5536f89cdb333fba5cbd87a5bb7dc14eb606ba6de002b18804ad7d255846bf', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '911a8f3b2246a47a0f488b5843ece78e1fb1de4a6c524f3110f095cc409d0e3e', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '9cddd09e36904f18a71dc437f367f849138e61e1a1367619eb5ee5393bef34d0', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', 'c2d4bd1d2e44a1a328e64a5c28eec70e91d7ce0dc4fb42d3af0c803c53c348c4', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', 'd9d84a51d597d9c65581fa033acaaf810ec916947ea5a1c13cf88aa6a5c3c270', '1');
INSERT INTO `vin` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '911a8f3b2246a47a0f488b5843ece78e1fb1de4a6c524f3110f095cc409d0e3e', '0');
INSERT INTO `vin` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2');
INSERT INTO `vin` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', 'c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '0');
INSERT INTO `vin` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '1');
INSERT INTO `vin` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '0');
INSERT INTO `vin` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '2');
INSERT INTO `vin` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '2');
INSERT INTO `vin` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '0');
INSERT INTO `vin` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '1');
INSERT INTO `vin` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '2');
INSERT INTO `vin` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '0');
INSERT INTO `vin` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '1');
INSERT INTO `vin` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '0');

-- ----------------------------
-- Table structure for vout
-- ----------------------------
DROP TABLE IF EXISTS `vout`;
CREATE TABLE `vout` (
  `txid` varchar(128) NOT NULL,
  `value` decimal(32,8) DEFAULT NULL,
  `n` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vout
-- ----------------------------
INSERT INTO `vout` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '60.51388699', '0');
INSERT INTO `vout` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '0.00000550', '1');
INSERT INTO `vout` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '0.00000551', '2');
INSERT INTO `vout` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '60.51387599', '0');
INSERT INTO `vout` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '0.00000550', '1');
INSERT INTO `vout` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '0.00000551', '2');
INSERT INTO `vout` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '60.51386399', '0');
INSERT INTO `vout` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '0.00000550', '1');
INSERT INTO `vout` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '0.00000551', '2');
INSERT INTO `vout` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '60.51385899', '0');
INSERT INTO `vout` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '0.00000550', '1');
INSERT INTO `vout` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '0.00000551', '2');
INSERT INTO `vout` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '60.51385299', '0');
INSERT INTO `vout` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '0.00000550', '1');
INSERT INTO `vout` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '0.00000551', '2');
INSERT INTO `vout` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '0.00000551', '0');
INSERT INTO `vout` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '60.51384598', '1');
INSERT INTO `vout` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '0.00000550', '2');

-- ----------------------------
-- Table structure for vout_address
-- ----------------------------
DROP TABLE IF EXISTS `vout_address`;
CREATE TABLE `vout_address` (
  `txid` varchar(128) NOT NULL,
  `n` int(11) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vout_address
-- ----------------------------
INSERT INTO `vout_address` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '0', '1AN9P63fc93v2wpH261Vyjrioq33W7p79e');
INSERT INTO `vout_address` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '1', '17vVkTTvyMGjSma1wyTdyofn5iQu6SbHXn');
INSERT INTO `vout_address` VALUES ('c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463', '2', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');
INSERT INTO `vout_address` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '0', '1AN9P63fc93v2wpH261Vyjrioq33W7p79e');
INSERT INTO `vout_address` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '1', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');
INSERT INTO `vout_address` VALUES ('01eb37131cf711b3c0be5fe73ae295034b77d7c43b11ad782a10ee679995c355', '2', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc');
INSERT INTO `vout_address` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '0', '1AN9P63fc93v2wpH261Vyjrioq33W7p79e');
INSERT INTO `vout_address` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '1', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');
INSERT INTO `vout_address` VALUES ('797cc922e90d8dd7f31abd30fb550934664ca84a46129be5c35da6cec7114611', '2', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc');
INSERT INTO `vout_address` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '0', '1AN9P63fc93v2wpH261Vyjrioq33W7p79e');
INSERT INTO `vout_address` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '1', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc');
INSERT INTO `vout_address` VALUES ('8cac9a03de22471e2ad319f575905e2c6f72315938f4f779d3b27d50bdb6d280', '2', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');
INSERT INTO `vout_address` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '0', '1AN9P63fc93v2wpH261Vyjrioq33W7p79e');
INSERT INTO `vout_address` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '1', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');
INSERT INTO `vout_address` VALUES ('00ee4f1c06a9d9eb96efaa010a686b0fc75cd54dd137b9a4ea146010e58a741c', '2', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc');
INSERT INTO `vout_address` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '0', '1A9eutkvZoQdNVevWGMbxwYNyQ5xtErjrc');
INSERT INTO `vout_address` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '1', '1AN9P63fc93v2wpH261Vyjrioq33W7p79e');
INSERT INTO `vout_address` VALUES ('2964363a9d0f8c430eef2d8151ed048cdcf3fbb55e1fa37d74bc19b094704088', '2', '12SL22kcHRLEcscSBzD3KdALhWZLaUk25d');
