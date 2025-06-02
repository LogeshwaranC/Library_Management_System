-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: library_schema
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `BookID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(100) NOT NULL,
  `Author` varchar(100) DEFAULT NULL,
  `Genre` varchar(50) DEFAULT NULL,
  `TotalCopies` int NOT NULL,
  `CopiesAvailable` int NOT NULL,
  `TimesBorrowed` int DEFAULT '0',
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`BookID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (4,'','ygd','\'ugvs\'',740,738,2,0),(5,'hello','Hello','Hello',25,22,27,0),(6,'java Basics','David Jhon','Education',45,5,3,1),(7,'demo','demo','demo',36,22,3,0),(8,'code','oderc','Coding',125,125,0,1),(9,'Coding Tutorial','david cooper','Education',95,75,40,1),(10,'demo book','demo author','Demo genre',50,50,0,0),(11,'The Great Gatsby','F. Scott Fitzgerald','Classic',5,5,0,1),(12,'To Kill a Mockingbird','Harper Lee','Classic',7,7,0,1),(13,'1984','George Orwell','Dystopian',10,10,0,1),(14,'The Hobbit','J.R.R. Tolkien','Fantasy',8,8,0,1),(15,'Pride and Prejudice','Jane Austen','Romance',6,6,0,1),(16,'The Catcher in the Rye','J.D. Salinger','Classic',4,4,0,1),(17,'The Da Vinci Code','Dan Brown','Thriller',9,9,0,1),(18,'Harry Potter and the Sorcerer\'s Stone','J.K. Rowling','Fantasy',15,15,0,1),(19,'The Alchemist','Paulo Coelho','Adventure',7,7,0,1),(20,'The Lord of the Rings','J.R.R. Tolkien','Fantasy',12,12,0,1),(21,'Moby Dick','Herman Melville','Classic',3,3,0,1),(22,'War and Peace','Leo Tolstoy','Historical',5,5,0,1),(23,'The Fault in Our Stars','John Green','Romance',10,10,0,1),(24,'Gone Girl','Gillian Flynn','Thriller',6,6,0,1),(25,'The Chronicles of Narnia','C.S. Lewis','Fantasy',11,11,0,1),(26,'Animal Farm','George Orwell','Political Satire',7,7,0,1),(27,'The Girl on the Train','Paula Hawkins','Thriller',8,8,0,1),(28,'Brave New World','Aldous Huxley','Dystopian',9,9,0,1),(29,'The Hunger Games','Suzanne Collins','Dystopian',14,14,0,1),(30,'Jane Eyre','Charlotte Brontë','Romance',6,6,0,1);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `librarymembers`
--

DROP TABLE IF EXISTS `librarymembers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `librarymembers` (
  `MemberID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MemberID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `librarymembers`
--

LOCK TABLES `librarymembers` WRITE;
/*!40000 ALTER TABLE `librarymembers` DISABLE KEYS */;
INSERT INTO `librarymembers` VALUES (1,'logesh','logesh','logesh'),(2,'member1','member1','member');
/*!40000 ALTER TABLE `librarymembers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `TransactionID` int NOT NULL AUTO_INCREMENT,
  `MemberID` int NOT NULL,
  `BookID` int NOT NULL,
  `Quantity` int NOT NULL,
  `TransactionDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Type` enum('Borrow','Return') DEFAULT NULL,
  PRIMARY KEY (`TransactionID`),
  KEY `MemberID` (`MemberID`),
  KEY `BookID` (`BookID`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`MemberID`) REFERENCES `librarymembers` (`MemberID`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `books` (`BookID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (4,1,5,24,'2025-06-02 01:58:17','Borrow'),(5,1,5,22,'2025-06-02 02:11:54','Return'),(6,1,5,3,'2025-06-02 02:34:36','Borrow'),(7,1,5,2,'2025-06-02 02:34:48','Return'),(8,1,7,3,'2025-06-02 03:27:11','Borrow'),(9,1,9,40,'2025-06-02 04:14:40','Borrow'),(10,1,9,20,'2025-06-02 04:15:04','Return'),(11,1,4,2,'2025-06-02 04:25:49','Borrow'),(12,1,6,3,'2025-06-02 04:27:40','Borrow'),(13,1,6,3,'2025-06-02 04:28:02','Return');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-02 15:59:06
