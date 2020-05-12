/*
Navicat MySQL Data Transfer

Source Server         : top7 区块浏览器
Source Server Version : 50729
Source Host           : 47.110.137.123:3306
Source Database       : xsvsp

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-05-12 16:35:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for address_hash_link
-- ----------------------------
DROP TABLE IF EXISTS `address_hash_link`;
CREATE TABLE `address_hash_link` (
  `address` varchar(128) DEFAULT NULL,
  `address_hash` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for block
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block` (
  `hash` varchar(100) NOT NULL COMMENT 'block hash ',
  `height` bigint(20) NOT NULL COMMENT 'block height',
  `timestamps` bigint(20) NOT NULL COMMENT 'block create timestamps',
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The table of block data ';

-- ----------------------------
-- Table structure for block_count
-- ----------------------------
DROP TABLE IF EXISTS `block_count`;
CREATE TABLE `block_count` (
  `height` bigint(22) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for chaintx
-- ----------------------------
DROP TABLE IF EXISTS `chaintx`;
CREATE TABLE `chaintx` (
  `txid` varchar(100) NOT NULL,
  `height` bigint(20) NOT NULL,
  PRIMARY KEY (`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='数据库链接信息';

-- ----------------------------
-- Table structure for genesis_address
-- ----------------------------
DROP TABLE IF EXISTS `genesis_address`;
CREATE TABLE `genesis_address` (
  `txid` varchar(255) NOT NULL,
  `issue_address` varchar(255) DEFAULT NULL,
  `issue_vout` int(11) DEFAULT NULL,
  `raise_address` varchar(255) DEFAULT NULL,
  `raise_vout` int(11) DEFAULT NULL,
  `raise_txid` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kyc_address
-- ----------------------------
DROP TABLE IF EXISTS `kyc_address`;
CREATE TABLE `kyc_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `id_number` varchar(255) DEFAULT NULL,
  `positive_img` varchar(512) DEFAULT NULL,
  `back_img` varchar(512) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mempooltx
-- ----------------------------
DROP TABLE IF EXISTS `mempooltx`;
CREATE TABLE `mempooltx` (
  `txid` varchar(100) NOT NULL,
  `height` bigint(20) NOT NULL,
  PRIMARY KEY (`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `init_issue_address` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Table structure for slppp
-- ----------------------------
DROP TABLE IF EXISTS `slppp`;
CREATE TABLE `slppp` (
  `txid` varchar(100) NOT NULL,
  `n` int(11) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `script` text,
  `hash` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`txid`,`n`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for token_assets
-- ----------------------------
DROP TABLE IF EXISTS `token_assets`;
CREATE TABLE `token_assets` (
  `token_id` varchar(128) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `token` bigint(32) DEFAULT NULL,
  `txid` varchar(128) DEFAULT NULL,
  `vout` int(11) DEFAULT NULL,
  `from_address` varchar(128) DEFAULT NULL,
  `time` bigint(11) DEFAULT NULL,
  `status` smallint(3) DEFAULT NULL COMMENT '(0：发行，1：增发，2：交易，3：销毁)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for token_destruction
-- ----------------------------
DROP TABLE IF EXISTS `token_destruction`;
CREATE TABLE `token_destruction` (
  `txid` varchar(128) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `n` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for utxo
-- ----------------------------
DROP TABLE IF EXISTS `utxo`;
CREATE TABLE `utxo` (
  `txid` varchar(100) NOT NULL,
  `n` int(11) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `addresspos` int(11) NOT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`txid`,`n`,`addresspos`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for utxo_token
-- ----------------------------
DROP TABLE IF EXISTS `utxo_token`;
CREATE TABLE `utxo_token` (
  `txid` varchar(100) NOT NULL,
  `n` int(11) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `script` text,
  PRIMARY KEY (`txid`,`n`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for vin
-- ----------------------------
DROP TABLE IF EXISTS `vin`;
CREATE TABLE `vin` (
  `txid` varchar(100) NOT NULL,
  `prevtxid` varchar(100) NOT NULL,
  `n` int(11) NOT NULL,
  PRIMARY KEY (`prevtxid`,`n`),
  KEY `txidkey` (`txid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for voutaddress
-- ----------------------------
DROP TABLE IF EXISTS `voutaddress`;
CREATE TABLE `voutaddress` (
  `txid` varchar(100) NOT NULL,
  `n` int(11) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `addresspos` int(11) NOT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`txid`,`n`,`addresspos`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for voutret
-- ----------------------------
DROP TABLE IF EXISTS `voutret`;
CREATE TABLE `voutret` (
  `txid` varchar(100) NOT NULL,
  `n` int(11) NOT NULL,
  `data` varchar(512) NOT NULL,
  PRIMARY KEY (`txid`,`n`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
