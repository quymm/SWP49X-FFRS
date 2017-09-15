CREATE DATABASE  IF NOT EXISTS `androiddb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `androiddb`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: androiddb
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `detail` varchar(250) NOT NULL,
  `square_image_url` varchar(250) NOT NULL,
  `rec_image_url` varchar(250) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_code_UNIQUE` (`category_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'CA001','Coffee','Freshly brewed coffee','squareImg1.png','recImg1.png'),(2,'CA002','Breakfast','Hearty, hot & full of flavour','squareImg2.png','recImg2.png'),(3,'CA003','Munchies','Perfectly baked goodies','squareImg3.png','recImg3.png'),(4,'CA004','Sandwiches','Fresh, healthy and tasty','squareImg4.png','recImg4.png'),(5,'CA005','Specialty Drinks','Special drinks for every taste','squareImg5.png','recImg5.png');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(45) NOT NULL,
  `title` varchar(45) NOT NULL,
  `detail` varchar(250) DEFAULT NULL,
  `date` datetime NOT NULL,
  `rate` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'PR001','Delicious','I like the drink and cake here.','2017-03-07 14:58:00',4),(2,'PR001','Good location','Come on in. This space is not so different from your neighborhood Starbucks.','2017-03-09 16:54:00',5),(3,'PR001','Strong Wifi','The wifi connection is so good. I love you. hjhj','2017-03-07 14:58:00',4),(4,'PR001','Too much sugar','how much sugar is in the Iced Cinnamon Almondmilk Macchiato when made with your sugar free Cinnamon syrup?','2017-03-08 14:58:00',3),(5,'string','string','string','2017-03-13 22:40:23',0),(6,'string','string','string','2017-03-14 02:49:25',0),(7,'string','string','string','2017-03-14 10:23:55',0),(8,'string','string','string','2017-03-14 10:23:55',0),(9,'string','string','string','2012-10-20 00:00:00',0),(10,'string','string','string ne','2012-10-20 00:00:00',0),(11,'stringsd dsf','string','string ne','2012-10-20 00:00:00',0),(12,'stringsd dsf','strinfdsf  dsfg','string ne','2012-10-20 00:00:00',0),(13,'stringsd dsf','strinfdsf  dsfg','string ne','2012-10-20 00:00:00',4),(14,'stringsd dsf','strinfdsf  dsfg','string ne','2012-10-20 00:00:00',4),(15,'PR002','Good','Everything is so so good','2017-03-14 00:00:00',4),(16,'PR002','Good','Everything is so so good','2017-03-14 00:00:00',4),(17,'PR002','Delicious','Drink is good','2017-03-14 00:00:00',5),(18,'PR003','Good','so good hjhj','2017-03-14 00:00:00',4),(19,'PR006','Good','Everything is good','2017-03-14 00:00:00',3),(20,'PR003','Best','Good','2017-03-14 00:00:00',3),(21,'PR001','Best','I can\'t find anywhere better than this place','2017-03-14 00:00:00',5),(22,'PR001','','','2017-03-14 00:00:00',3),(23,'PR002','Best','Best of the best','2017-03-14 00:00:00',2),(24,'PR005','Good','delicious','2017-03-14 00:00:00',4),(25,'PR004','best','good','2017-03-14 00:00:00',4);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(45) NOT NULL,
  `category_code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `detail` varchar(250) NOT NULL,
  `old_price` double NOT NULL,
  `new_price` double DEFAULT NULL,
  `number_of_lover` int(11) NOT NULL DEFAULT '0',
  `image_url` varchar(250) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_code_UNIQUE` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'PR001','CA001','Choco Frappe','Chocolate Whirl',13.5,6,3221,'11.png'),(2,'PR002','CA001','Caramel Frappe','Decaf Colombia',7.85,6.8,4358,'21.png'),(3,'PR003','CA001','Kick Frappe','A deliciously creamy Coffee Kick Frappé, topped with irresistible whipped cream and indulgent Coffee Drizzle. Perfect combination',12.35,10.05,4452,'31.png'),(4,'PR004','CA001','Cappuccino','Decaf Colombia',8.5,7,324,'41.png'),(5,'PR005','CA002','Chocolate Muffin','A rich chocolate muffin with a creamy caramel center',4.3,4,435,'52.png'),(6,'PR006','CA002','Classic Bagel','A classic New York style bagel. Thick, chewy crust and soft',2,1.9,325,'62.png'),(7,'PR007','CA002','Chicken & Veggie Pie ','Made with tender 100% chicken and baked fresh in store every day',6.5,3.3,3000,'72.png'),(8,'PR008','CA002','Choco Cookies ','Sweet cream cheese with strawberry and raspberry jam',3.8,3,377,'82.png'),(9,'PR009','CA002','Strawberry Pancakes ','Light and fluffy pancakes packed with strawberries',9,7,153,'92.png');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-05 14:18:14
