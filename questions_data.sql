-- MySQL dump 10.13  Distrib 8.4.11, for macos15 (x86_64)
--
-- Host: localhost    Database: online_quiz_db
-- ------------------------------------------------------
-- Server version	8.4.11

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
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'Which language is used for Android development?','Java','HTML','CSS','SQL','A'),(2,'Which keyword creates an object in Java?','class','new','void','static','B'),(3,'Which database are we using?','Oracle','MongoDB','MySQL','SQLite','C'),(4,'Which method starts a Java program?','start()','run()','main()','init()','C'),(5,'Which component creates a Java GUI window?','JFrame','Scanner','String','Array','A'),(6,'Who developed Java?','Microsoft','James Gosling','Google','Dennis Ritchie','B'),(7,'Which keyword creates an object?','class','void','new','static','C'),(8,'Which is not a Java primitive type?','int','boolean','String','char','C'),(9,'Which method is the entry point of Java?','start()','main()','run()','init()','B'),(10,'Which keyword is used for inheritance?','extends','import','return','package','A'),(11,'Which symbol ends a Java statement?',':',';',',','.','B'),(12,'Which class is used to create a GUI window in Swing?','Scanner','JFrame','String','System','B'),(13,'Which keyword prevents inheritance?','static','void','final','public','C'),(14,'Which data type stores true or false?','int','boolean','double','char','B'),(15,'Which operator checks equality of primitive values?','=','==','!=','&&','B'),(16,'Which loop executes at least once?','for','while','do-while','foreach','C'),(17,'Which keyword handles exceptions?','catch','throwing','error','finalize','A'),(18,'Which package contains Scanner?','java.io','java.util','java.sql','java.awt','B'),(19,'Which collection stores unique elements?','ArrayList','LinkedList','HashSet','Vector','C'),(20,'Which keyword refers to the current object?','super','this','self','current','B'),(21,'Which access modifier allows access from anywhere?','private','protected','public','default','C'),(22,'Which method compares String contents?','==','equals()','compareToInt()','matchString()','B'),(23,'Which SQL command retrieves data?','INSERT','UPDATE','SELECT','DELETE','C'),(24,'Which JDBC class executes a prepared SQL statement?','JFrame','PreparedStatement','Scanner','FileReader','B'),(25,'Which component displays a single-line text field in Swing?','JLabel','JButton','JTextField','JFrame','C');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-24 16:25:35
