CREATE DATABASE  IF NOT EXISTS `uservault` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `uservault`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: uservault
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_seller`
--

DROP TABLE IF EXISTS `admin_seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_seller` (
  `admin_id` int unsigned NOT NULL,
  `seller_id` int unsigned NOT NULL,
  PRIMARY KEY (`seller_id`),
  KEY `admin_seller_seller_id_foreign` (`seller_id`),
  KEY `admin_seller_admin_id_foreign` (`admin_id`),
  CONSTRAINT `admin_seller_admin_id_foreign` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`),
  CONSTRAINT `admin_seller_seller_id_foreign` FOREIGN KEY (`seller_id`) REFERENCES `sellers` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `admin_id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `birthday` date NOT NULL,
  `password_hash` text NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `admins_username_unique` (`username`),
  UNIQUE KEY `admins_email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `bill_id` int unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int unsigned NOT NULL,
  `time_spent_talking` int unsigned NOT NULL,
  `total_cost` double unsigned NOT NULL,
  `month_issued` date NOT NULL,
  `status` varchar(255) NOT NULL,
  `total_sms` int NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `bill_client_id_foreign` (`client_id`),
  CONSTRAINT `bill_client_id_foreign` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `calls`
--

DROP TABLE IF EXISTS `calls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calls` (
  `phone_number` varchar(255) NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `duration` int NOT NULL,
  PRIMARY KEY (`phone_number`,`timestamp`),
  CONSTRAINT `calls_phone_number_foreign` FOREIGN KEY (`phone_number`) REFERENCES `phonenumbers` (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_seller`
--

DROP TABLE IF EXISTS `client_seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_seller` (
  `client_id` int unsigned NOT NULL,
  `seller_id` int unsigned NOT NULL,
  PRIMARY KEY (`client_id`),
  KEY `client_seller_seller_id_foreign` (`seller_id`),
  CONSTRAINT `client_seller_client_id_foreign` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
  CONSTRAINT `client_seller_seller_id_foreign` FOREIGN KEY (`seller_id`) REFERENCES `sellers` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `client_id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `birthday` date NOT NULL,
  `VAT` varchar(255) NOT NULL,
  `password_hash` text NOT NULL,
  PRIMARY KEY (`client_id`),
  UNIQUE KEY `clients_username_unique` (`username`),
  UNIQUE KEY `clients_email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phonenumbers`
--

DROP TABLE IF EXISTS `phonenumbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phonenumbers` (
  `phone_number` varchar(255) NOT NULL,
  `client_id` int unsigned NOT NULL,
  `program_id` int unsigned NOT NULL,
  PRIMARY KEY (`phone_number`),
  KEY `phonenumber_program_id_foreign` (`program_id`),
  KEY `phonenumber_client_id_foreign` (`client_id`),
  CONSTRAINT `phonenumber_client_id_foreign` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
  CONSTRAINT `phonenumber_program_id_foreign` FOREIGN KEY (`program_id`) REFERENCES `programs` (`program_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `programs`
--

DROP TABLE IF EXISTS `programs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programs` (
  `program_id` int unsigned NOT NULL AUTO_INCREMENT,
  `program_name` varchar(255) NOT NULL,
  `fixed_cost` double NOT NULL,
  `cost_per_minute` double NOT NULL,
  `cost_per_sms` double NOT NULL,
  `available_minutes` int NOT NULL,
  `available_sms` int NOT NULL,
  PRIMARY KEY (`program_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sellers`
--

DROP TABLE IF EXISTS `sellers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sellers` (
  `seller_id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `birthday` date NOT NULL,
  `password_hash` text NOT NULL,
  PRIMARY KEY (`seller_id`),
  UNIQUE KEY `sellers_username_unique` (`username`),
  UNIQUE KEY `sellers_email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms`
--

DROP TABLE IF EXISTS `sms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms` (
  `phone_number` varchar(255) NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `message` text NOT NULL,
  PRIMARY KEY (`phone_number`,`timestamp`),
  CONSTRAINT `sms_phone_number_foreign` FOREIGN KEY (`phone_number`) REFERENCES `phonenumbers` (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'uservault'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `MonthlyBillCalculation` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `MonthlyBillCalculation` ON SCHEDULE EVERY 1 MONTH STARTS '2024-06-01 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DECLARE finished INTEGER DEFAULT 0;
    DECLARE client_id INT;
    DECLARE client_cursor CURSOR FOR SELECT client_id FROM Clients;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;

    OPEN client_cursor;

    get_client: LOOP
        FETCH client_cursor INTO client_id;
        IF finished = 1 THEN
            LEAVE get_client;
        END IF;

        -- Call the CalculateBill procedure for each client
        CALL CalculateBill(client_id, CURRENT_DATE);
    END LOOP get_client;

    CLOSE client_cursor;
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'uservault'
--
/*!50003 DROP FUNCTION IF EXISTS `LEVENSHTEIN` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `LEVENSHTEIN`(s1 VARCHAR(255), s2 VARCHAR(255)) RETURNS int
    DETERMINISTIC
BEGIN
  DECLARE s1_len, s2_len, i, j, c, c_temp, cost INT;
  DECLARE s1_char CHAR;
  -- max strlen=255
  DECLARE cv0, cv1 VARBINARY(256);
  SET s1_len = CHAR_LENGTH(s1), s2_len = CHAR_LENGTH(s2), cv1 = 0x00, j = 1, i = 1, c = 0;
  IF s1 = s2 THEN
    RETURN 0;
  ELSEIF s1_len = 0 THEN
    RETURN s2_len;
  ELSEIF s2_len = 0 THEN
    RETURN s1_len;
  ELSE
    WHILE j <= s2_len DO
      SET cv1 = CONCAT(cv1, UNHEX(HEX(j))), j = j + 1;
    END WHILE;
    WHILE i <= s1_len DO
      SET s1_char = SUBSTRING(s1, i, 1), c = i, cv0 = UNHEX(HEX(i)), j = 1;
      WHILE j <= s2_len DO
        SET cost = IF(s1_char = SUBSTRING(s2, j, 1), 0, 1);
        SET c_temp = CONV(HEX(SUBSTRING(cv1, j, 1)), 16, 10) + cost;
        IF c > c_temp THEN SET c = c_temp; END IF;
          SET c_temp = CONV(HEX(SUBSTRING(cv1, j+1, 1)), 16, 10) + 1;
        IF c > c_temp THEN 
          SET c = c_temp; 
        END IF;
        SET cv0 = CONCAT(cv0, UNHEX(HEX(c))), j = j + 1;
      END WHILE;
      SET cv1 = cv0, i = i + 1;
    END WHILE;
  END IF;
  RETURN c;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CalculateBill` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CalculateBill`(IN client_id_p int, IN month_issued_p date)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE cur_phone_number VARCHAR(255);
    DECLARE cur CURSOR FOR SELECT phone_number FROM PhoneNumbers WHERE client_id = client_id_p;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    -- Initialize total_cost with 0
    SET @total_cost = 0;
    SET @total_minutes = 0;
    SET @total_sms = 0;

    OPEN cur;

    read_loop:
    LOOP
        FETCH cur INTO cur_phone_number;
        IF done THEN
            LEAVE read_loop;
        END IF;

        -- Check if the bill already exists for the given month and client
        IF EXISTS (SELECT *
                   FROM Bills
                   WHERE client_id = client_id_p
                     AND MONTH(month_issued) = MONTH(month_issued_p)
                     AND YEAR(month_issued) = YEAR(month_issued_p)) THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bill already exists for the given month and client';
        END IF;

        -- Calculate the total minutes for the phone number for the given month
        SET @total_minutes = @total_minutes + COALESCE((SELECT SUM(duration)
                                                        FROM Calls
                                                        WHERE phone_number = cur_phone_number
                                                          AND MONTH(timestamp) = MONTH(month_issued_p)
                                                          AND YEAR(timestamp) = YEAR(month_issued_p)), 0);
        SET @total_sms = @total_sms + COALESCE((SELECT COUNT(*)
                                                FROM SMS
                                                WHERE phone_number = cur_phone_number
                                                  AND MONTH(timestamp) = MONTH(month_issued_p)
                                                  AND YEAR(timestamp) = YEAR(month_issued_p)), 0);

        -- Calculate the total cost based on program details
        SET @program_id = COALESCE((SELECT program_id FROM PhoneNumbers WHERE phone_number = cur_phone_number), 0);
        SET @fixed_cost = COALESCE((SELECT fixed_cost FROM Programs WHERE program_id = @program_id), 0);
        SET @cost_per_minute = COALESCE((SELECT cost_per_minute FROM Programs WHERE program_id = @program_id), 0);
        SET @cost_per_sms = COALESCE((SELECT cost_per_sms FROM Programs WHERE program_id = @program_id), 0);
        SET @available_minutes = COALESCE((SELECT available_minutes FROM Programs WHERE program_id = @program_id), 0);
        SET @available_sms = COALESCE((SELECT available_sms FROM Programs WHERE program_id = @program_id), 0);

        -- Calculate the total cost
        SET @total_cost = @total_cost + @fixed_cost;
        IF @total_minutes > @available_minutes THEN
            SET @total_cost = @total_cost + ((@total_minutes - @available_minutes) * @cost_per_minute);
        END IF;
        IF @total_sms > @available_sms THEN
            SET @total_cost = @total_cost + ((@total_sms - @available_sms) * @cost_per_sms);
        END IF;
    END LOOP;

    CLOSE cur;

    -- Insert the bill into the Bill table
    INSERT INTO Bills(client_id, time_spent_talking, total_cost, month_issued, status, total_sms)
    VALUES (client_id_p, @total_minutes, @total_cost, month_issued_p, 'Issued', @total_sms);
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

-- Dump completed on 2024-06-10  2:35:40
