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
INSERT INTO `bm_flow_audit_category` VALUES (120020,'生产配置','120020',0,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'12002,120020001,120020002,120020003','生产配置'),(120030,'生产指令单','120030',0,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'120030,120030001','生产指令单'),(120040,'批签发','120040',0,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'120040,120040001','批签发'),(120050,'批记录','120050',0,'1','1','2024-10-31 18:13:51','2024-10-31 18:13:51',0,'120050,120050001','批记录'),(120020001,'记录审批','120020001',120020,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'120020001','生产配置/记录审批'),(120020002,'工艺审批','120020002',120020,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'120020002','生产配置/工艺审批'),(120020003,'生产BOM审批','120020003',120020,'1',NULL,'2024-04-09 10:48:26',NULL,0,'120020003','生产配置/生产BOM审批'),(120020004,'操作规程审批','120020004',120020,'1','1','2024-07-31 15:42:15','2024-07-31 15:42:15',0,'120020004','生产配置/操作规程启用审核'),(120030001,'指令单审批','120030001',120030,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'120030001','生产指令单/指令单审批'),(120040001,'批签发审核','120040001',120040,'1','1','2024-04-09 10:48:26','2024-03-08 18:18:10',0,'120040001','批签发/批签发审核'),(120050001,'批记录审核','120050001',120050,'1','1','2024-10-31 18:13:51','2024-10-31 18:13:51',0,'120050001','批记录/批记录审核');
/*!40000 ALTER TABLE `bm_flow_audit_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'bmos_mes'
--
/*!50003 DROP PROCEDURE IF EXISTS `insertData` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `insertData`()
BEGIN
    DECLARE i INT DEFAULT 0;
    WHILE i < 100 DO 
    INSERT INTO bm_procedure VALUES (CONV(SUBSTRING(uuid(), 1, 8), 16, 10), CONV(SUBSTRING(uuid(), 1, 8), 16, 10), uuid()); 
    SET i = i + 1;
        END WHILE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-19 15:16:49
