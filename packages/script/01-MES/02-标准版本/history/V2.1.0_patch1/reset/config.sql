use bmos_platform;
truncate table bmos_platform.bp_business_parameter;

-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: bmos_platform
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `bp_business_parameter`
--

LOCK TABLES `bp_business_parameter` WRITE;
/*!40000 ALTER TABLE `bp_business_parameter` DISABLE KEYS */;
INSERT INTO `bp_business_parameter` VALUES (1,'application',NULL,NULL,NULL,NULL,NULL,NULL,'[{\"label\": \"生产\",\"value\": \"生产\",\"URL\": \"\"}, { \"label\": \"平台\",\"value\": \"平台\",\"URL\": \"\" }]',0,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100001,'platform.sys.time-format','yyyy-MM-dd HH:mm:ss','STRING','BUSINESS','平台','记录作业日期组件时间格式',100020,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100004,'platform.sys.app-lock-screen-time','15','NUMBER','BUSINESS','平台','移动端锁屏时间，单位：分钟',100030,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100005,'platform.sys.web-lock-screen-time','15','NUMBER','BUSINESS','平台','网页端锁屏时间，单位：分钟',100040,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100006,'platform.sys.web-lock-screen-hotkey','[\"Ctrl\",\"L\"]','JSON','BUSINESS','平台','网页端锁屏快捷键',100050,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100007,'platform.sys.client-name','佰墨思','STRING','BUSINESS','平台','部门管理根节点名称',100010,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100008,'platform.sys.language','{\"中文\":\"zh_CN\",\"英文\":\"en_US\",\"俄文\":\"ru_RU\"}','JSON','BUSINESS','平台','系统语言',100080,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(100011,'platform.user.pwd-rule.character','{\n  \"lowerCase\": true,\n  \"upperCase\": true,\n  \"digit\": true,\n  \"specialCharacters\": []\n}','STRING','BUSINESS','平台','密码复杂度校验配置：lowerCase：是否必须有小写字母；upperCase：是否必须有大写字母；digit：是否必须有数字；specialCharacters：是否必须有specialCharacters配置的字符',100110,'',1,NULL,NULL,NULL,NULL,0),(100012,'platform.user.pwd-rule.minLen','6','STRING','BUSINESS','平台','用户密码最小密码长度，限制6-24内整数',100120,'',1,NULL,NULL,NULL,NULL,0),(100013,'platform.user.pwd-rule.tryNum','5','STRING','BUSINESS','平台','用户密码尝试次数，限制0-24内整数',100130,'',1,NULL,NULL,NULL,NULL,0),(100014,'platform.user.pwd-rule.hisNum','0','STRING','BUSINESS','平台','用户历史密码个数，限制0-6内整数',100140,'',1,NULL,NULL,NULL,NULL,0),(100015,'platform.user.pwd-rule.validity','365','STRING','BUSINESS','平台','用户密码有效期，单位为：天',100150,'',1,NULL,NULL,NULL,NULL,0),(100017,'platform.sys.app-msg-polling-time','30','NUMBER','BUSINESS','平台','移动端消息轮询时间，单位：秒',100160,'',1,'1','1','2024-04-09 10:48:26','2024-04-09 10:48:26',0),(100018,'platform.sys.web-msg-polling-time','30','NUMBER','BUSINESS','平台','网页端消息轮询时间，单位：秒',100170,'',1,'1','1','2024-04-09 10:48:26','2024-04-09 10:48:26',0),(100019,'platform.sys.project-config','[{\"label\":\"佰墨思\",\"key\":\"bmos\"},{\"label\":\"白俄\",\"key\":\"be\"},{\"label\":\"康盛科泰\",\"key\":\"kskt\"},{\"label\":\"华兰生物\",\"key\":\"hlsw\"}]','JSON','BUSINESS','平台','系统项目配置',100190,'',1,'1','1','2024-04-09 10:48:26','2024-04-09 10:48:26',0),(100020,'platform.sys.license.isRequired','true','STRING','BUSINESS','平台','系统是否需要授权',100200,'',1,'1','1','2024-06-12 16:09:06','2024-06-12 16:09:06',0),(120001,'mes.record.margin','{\"left\":\"10\", \"right\":\"10\", \"top\":\"10\", \"bottom\":\"10\"}','JSON','BUSINESS','生产','批记录页边，单位：毫米',120010,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(120002,'mes.record.empty-data','N/A','STRING','BUSINESS','生产','记录作业空值',120020,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(120003,'mes.record.error-data','ERROR!','STRING','BUSINESS','生产','记录作业计算异常值',120030,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(120004,'mes.schedule.plan-invalid','-废','STRING','BUSINESS','生产','生产指令单作废标记',120040,'',1,'1','1','2024-03-08 18:17:52','2024-03-08 18:17:52',0),(120005,'mes.release.over-level-data','/','STRING','BUSINESS','生产','批签发单元格空值',120050,'',1,NULL,NULL,NULL,NULL,0),(120006,'mes.ProductionPlanType','{\"PRODUCT\":\"A\",\"EXPERIMENT\":\"B\",\"VERIFY\":\"C\"}','JSON','BUSINESS','生产','生产指令单类型标识:PRODUCT-生产批次、EXPERIMENT-实验批次、VERIFY-验证批次',120060,'',1,'1','1','2024-07-31 15:42:15','2024-07-31 15:42:15',0),(120007,'mes.record.font.size','16','NUMBER','BUSINESS','生产','批记录组件默认字号,字号范围[5,72]',120070,'',1,'1','1','2024-07-31 15:42:15','2024-07-31 15:42:15',0),(120008,'mes.record.default.font','SimSun','STRING','BUSINESS','生产','批记录组件默认字体，宋体：SimSun，黑体：SimHei，微软雅黑：YaHei，仿宋：FangSong',120080,'',1,'1','1','2024-07-31 15:42:15','2024-07-31 15:42:15',0),(120009,'mes.weigh.require.advance','15','NUMBER','BUSINESS','生产','称量需求提前规划时间，单位：日，范围[0,999]',120090,'',1,'1','1','2024-07-31 15:42:15','2024-07-31 15:42:15',0),(120010,'mes.material.dying.period','15','NUMBER','BUSINESS','生产','物料临期提醒默认时间，单位：日，范围[0,999]',120100,'',1,'1','1','2024-07-31 15:42:15','2024-07-31 15:42:15',0);
/*!40000 ALTER TABLE `bp_business_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bmos_platform'
--

--
-- Dumping routines for database 'bmos_platform'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-31 15:42:54
