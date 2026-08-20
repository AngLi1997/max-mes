use bmos_mes;
truncate table bmos_mes.bm_flow_audit_category;-- MariaDB dump 10.19  Distrib 10.4.26-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: bmos_mes
-- ------------------------------------------------------
-- Server version	10.4.26-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping routines for database 'bmos_mes'
--

--
-- Dumping data for table `bm_flow_audit_category`
--

LOCK TABLES `bm_flow_audit_category` WRITE;
/*!40000 ALTER TABLE `bm_flow_audit_category` DISABLE KEYS */;
INSERT INTO `bm_flow_audit_category` VALUES (120020,'记录配置','120020',0,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120020,120020001,120020002','记录配置'),(120030,'生产计划','120030',0,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120030,120030001','生产计划'),(120040,'批签发','120040',0,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120040,120040001','批签发'),(120020001,'记录审批','120020001',120020,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120020001','记录配置-记录审批'),(120020002,'工艺审批','120020002',120020,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120020002','记录配置-工艺审批'),(120030001,'生产计划审批','120030001',120030,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120030001','生产计划-生产计划审批'),(120040001,'批签发审核','120040001',120040,'1','1','2024-03-08 18:18:50','2024-03-08 18:18:10',0,'120040001','批签发-批签发审核');
/*!40000 ALTER TABLE `bm_flow_audit_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bmos_mes'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-14 11:58:01
