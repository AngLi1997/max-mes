use bmos_scheduler;
truncate table bmos_scheduler.xxl_job_user;
truncate table bmos_scheduler.xxl_job_group;
truncate table bmos_scheduler.xxl_job_info;
truncate table bmos_scheduler.xxl_job_lock;

-- MySQL dump 10.13  Distrib 8.0.38, for macos12.7 (arm64)
--
-- Host: localhost    Database: bmos_scheduler
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Dumping data for table `xxl_job_user`
--

LOCK TABLES `xxl_job_user` WRITE;
/*!40000 ALTER TABLE `xxl_job_user` DISABLE KEYS */;
INSERT INTO `xxl_job_user` (`id`, `username`, `password`, `role`, `permission`) VALUES (1,'admin','0192023a7bbd73250516f069df18b500',1,NULL);
/*!40000 ALTER TABLE `xxl_job_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `xxl_job_group`
--

LOCK TABLES `xxl_job_group` WRITE;
/*!40000 ALTER TABLE `xxl_job_group` DISABLE KEYS */;
INSERT INTO `xxl_job_group` (`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (1,'bmos-mes-service','制造执行系统',0,NULL,'2024-06-12 16:09:07'),(2,'bmos-wms-service','仓储管理系统',0,NULL,'2024-06-12 16:09:07'),(3,'bmos-platform-service','制药管理平台',0,NULL,'2024-06-12 16:09:07');
/*!40000 ALTER TABLE `xxl_job_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `xxl_job_info`
--

LOCK TABLES `xxl_job_info` WRITE;
/*!40000 ALTER TABLE `xxl_job_info` DISABLE KEYS */;
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (1,1,'刷新MES物料批次可用状态','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','updateStorageBatchAvailable','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(2,2,'刷新WMS货品批次可用状态','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','refreshInventoryBatchAvailable','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(3,3,'刷新账户密码过期状态','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0 0 1 * * ?','DO_NOTHING','FIRST','userPwdExpireValid','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(4,3,'设备占用心跳释放','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0/10 * * * * ?','DO_NOTHING','FIRST','equipmentHeart','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(5,1,'操作规程状态修改','2024-07-31 15:42:15','2024-07-31 15:42:15','admin','','CRON','0 5 0 * * ?','DO_NOTHING','FIRST','updateOperateRuleVersion','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2024-07-31 15:42:15','',1,0,20240731154215),(6,1,'工艺版本生效状态修改','2024-08-22 19:03:50','2024-08-22 19:03:50','admin','','CRON','0 8 0 * * ?','DO_NOTHING','FIRST','updateProcessVersionActionState','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2024-08-22 19:03:50','',1,0,1720627680000),(7,1,'定时删除在minio中的验证批记录','2024-11-27 11:37:32','2024-11-27 11:37:32','admin','','CRON','0 0 1 * * ?','DO_NOTHING','FIRST','removeVerifyArchive','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2024-11-27 11:37:32','',1,0,0),(8,3,'账号锁定时间到了之后自动解锁','2025-03-17 17:11:51','2025-03-17 17:11:51','admin','','CRON','0 * * * * ?','DO_NOTHING','FIRST','userUnLockExpireValid','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2024-12-23 13:52:42','',1,1736478960000,1736479020000),(9,1,'物料临期提醒消息发送','2025-03-17 17:11:51','2025-03-17 17:11:51','admin','','CRON','0 0 2 * * ?','DO_NOTHING','FIRST','MaterialExpireForeWarning','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2025-03-17 17:11:51','',1,1737053999000,1737136800000),(10,1,'称量中心定时刷新称量需求过期','2025-03-17 17:11:51','2025-03-17 17:11:51','admin','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','updateWeighCenterRequirementExpired','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2025-03-17 17:11:51','',1,1737053999000,1737136800000),(11,3,'定时刷新设备属性状态','2025-05-20 18:51:08','2025-05-20 18:51:08','admin','','CRON','0 * * * * ?','DO_NOTHING','FIRST','equipmentPropertiesStatus','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2025-05-20 18:51:08','',1,1745718780000,1745718840000);
/*!40000 ALTER TABLE `xxl_job_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `xxl_job_lock`
--

LOCK TABLES `xxl_job_lock` WRITE;
/*!40000 ALTER TABLE `xxl_job_lock` DISABLE KEYS */;
INSERT INTO `xxl_job_lock` (`lock_name`) VALUES ('schedule_lock');
/*!40000 ALTER TABLE `xxl_job_lock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-20 18:51:08
