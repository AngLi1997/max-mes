use bmos_mes;
truncate table bmos_mes.bm_flow_audit_category;

-- MySQL dump 10.13  Distrib 8.0.38, for macos12.7 (arm64)
--
-- Host: localhost    Database: bmos_mes
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
-- Dumping data for table `bm_flow_audit_category`
--

LOCK TABLES `bm_flow_audit_category` WRITE;
/*!40000 ALTER TABLE `bm_flow_audit_category` DISABLE KEYS */;
INSERT INTO `bm_flow_audit_category` (`id`, `name`, `code`, `parent_id`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tree_code`, `tree_name`) VALUES (12002001,'生产配置','12002001',0,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'12002001,12002000101,12002000201,12002000301','生产配置'),(12002000301,'生产BOM审批','12002000301',12002001,'1',NULL,'2024-04-09 10:48:26',NULL,0,'12002000301','生产配置/生产BOM审批');
/*!40000 ALTER TABLE `bm_flow_audit_category` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-25 15:11:59
