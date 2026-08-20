use bmos_scheduler;
truncate table bmos_scheduler.xxl_job_user;
truncate table bmos_scheduler.xxl_job_group;
truncate table bmos_scheduler.xxl_job_info;
truncate table bmos_scheduler.xxl_job_lock;

-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: bmos_scheduler
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
-- Dumping data for table `xxl_job_user`
--

LOCK TABLES `xxl_job_user` WRITE;
/*!40000 ALTER TABLE `xxl_job_user` DISABLE KEYS */;
INSERT INTO `xxl_job_user` VALUES (1,'admin','0192023a7bbd73250516f069df18b500',1,NULL);
/*!40000 ALTER TABLE `xxl_job_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `xxl_job_group`
--

LOCK TABLES `xxl_job_group` WRITE;
/*!40000 ALTER TABLE `xxl_job_group` DISABLE KEYS */;
INSERT INTO `xxl_job_group` VALUES (1,'bmos-mes-service','制造执行系统',0,NULL,'2024-06-12 16:09:07'),(2,'bmos-wms-service','仓储管理系统',0,NULL,'2024-06-12 16:09:07'),(3,'bmos-platform-service','制药管理平台',0,NULL,'2024-06-12 16:09:07');
/*!40000 ALTER TABLE `xxl_job_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `xxl_job_info`
--

LOCK TABLES `xxl_job_info` WRITE;
/*!40000 ALTER TABLE `xxl_job_info` DISABLE KEYS */;
INSERT INTO `xxl_job_info` VALUES (1,1,'刷新MES物料批次可用状态','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','updateStorageBatchAvailable','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(2,2,'刷新WMS货品批次可用状态','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','refreshInventoryBatchAvailable','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(3,3,'刷新账户密码过期状态','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0 0 1 * * ?','DO_NOTHING','FIRST','userPwdExpireValid','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',1,0,0),(4,3,'设备占用心跳释放','2024-06-12 16:09:07','2024-06-12 16:09:07','admin','','CRON','0/10 * * * * ?','DO_NOTHING','FIRST','equipmentHeart','','SERIAL_EXECUTION',0,0,'BEAN','','2024-06-12 16:09:07','2024-06-12 16:09:07','',0,0,0);
/*!40000 ALTER TABLE `xxl_job_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `xxl_job_lock`
--

LOCK TABLES `xxl_job_lock` WRITE;
/*!40000 ALTER TABLE `xxl_job_lock` DISABLE KEYS */;
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');
/*!40000 ALTER TABLE `xxl_job_lock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bmos_scheduler'
--

--
-- Dumping routines for database 'bmos_scheduler'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-12 16:09:08
