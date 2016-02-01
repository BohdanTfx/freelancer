-- MySQL dump 10.13  Distrib 5.6.28, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: freelancerdb
-- ------------------------------------------------------
-- Server version	5.6.28-0ubuntu0.15.10.1

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `lang` enum('en','uk-UA') DEFAULT 'en',
  `reg_url` varchar(150) DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  `uuid` varchar(140) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  `salt` varchar(50) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `reg_url` (`reg_url`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=16384;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'adminfreelancer@gnail.com','admin','Dmytro','Shapovalov','en',NULL,NULL,NULL,0,'\0','admin',NULL);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quest_id` int(11) DEFAULT NULL,
  `correct` bit(1) NOT NULL DEFAULT b'0',
  `name` varchar(100) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_answer_question_id` (`quest_id`),
  CONSTRAINT `FK_answer_question_id` FOREIGN KEY (`quest_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cust_id` int(11) DEFAULT NULL,
  `dev_id` int(11) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `skype` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_contact_customer_id` (`cust_id`),
  KEY `FK_contact_developer_id` (`dev_id`),
  CONSTRAINT `FK_contact_customer_id` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_contact_developer_id` FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=1092;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (1,1,NULL,'02231768994','kumar99923',0,'\0'),(2,2,NULL,'34536658294','ryzkov',0,'\0'),(3,3,NULL,'73636489021','strinic_ssde',0,'\0'),(4,4,NULL,'55139204752','belousov221',0,'\0'),(5,5,NULL,'22395849393','mungki_33e',0,'\0'),(6,NULL,1,'28495748932','iuliana_ssa',0,'\0'),(7,NULL,2,'89884736483','ivanov',0,'\0'),(8,NULL,3,'89047584214','ss_hgandres',0,'\0'),(9,NULL,4,'87274859032','hajji33422',0,'\0'),(10,NULL,5,'84759298412','stanisa',0,'\0'),(11,NULL,6,'22374859204','ognev0002',0,'\0'),(12,NULL,7,'73847763748','guptassd34',0,'\0'),(13,NULL,8,'65784878362','lamine_pflf',0,'\0'),(14,NULL,9,'15467485902','stefan4444',0,'\0'),(15,NULL,10,'98057869462','llgogd334',0,'\0');
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `zone` timestamp NULL DEFAULT NULL,
  `lang` enum('en','uk-UA') DEFAULT 'en',
  `uuid` varchar(140) DEFAULT NULL,
  `reg_url` varchar(150) DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  `salt` varchar(50) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `overview` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `reg_url` (`reg_url`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=2340;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'kumar@gmail.com','Kumar','Anil','Kumar','2010-11-23 19:34:01','en',NULL,NULL,'2004-06-17 00:00:00',0,'\0','Anil',NULL,NULL),(2,'ryzkov@gmail.com','Ryzkov','Anton','Ryzkov','2009-05-02 10:23:21','en',NULL,NULL,'2010-06-21 00:00:00',0,'\0','Ryzkov',NULL,NULL),(3,'strinic@gmail.com','Strinic','Ivan','Strinic','2006-02-19 14:03:34','en',NULL,NULL,'2003-02-11 00:00:00',0,'\0','Strinic',NULL,NULL),(4,'belousov@gmail.com','Belousov','Nikolai','Belousov','2009-11-28 20:26:01','en',NULL,NULL,'2009-06-13 00:00:00',0,'\0','Belousov',NULL,NULL),(5,'mungki@gmail.com','Laulau','MungKi','Lau','2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Lau',NULL,NULL),(6,'sadamaza@gmail.com','6e4938ca228ebe9b6d54b1989940fe5e2c714d93711f4cf8e79f8689f59dc3c8','sada','maza',NULL,NULL,'7131b214-c305-4dcc-93da-a73f6bab5ba4',NULL,'2016-01-21 00:00:00',NULL,NULL,'UTQZNuHYMAVzabbGSGfTFTUzHbgsUALugVoggepYNlLfrzYQoq',NULL,NULL),(7,'fff@mail.ru','79e5b9fe1d8a68d3f5eea4affe27348fb199a364bd10b261b50bc61eefdf6fd8','fff','fff',NULL,NULL,'95edc503-e7f1-46d1-9606-2332429d62d1',NULL,'2016-01-21 18:30:32',NULL,NULL,'eWmRkNIsAYIZiiKtibBwDxBbbRfOerkUibGUgZoTjKmfjITwxA',NULL,NULL),(8,'cust@gmail.com','5a5b75ee3b98d72617ca09ad4f7947f547b04284c848c88c0b135ca6354d43f7','cust','cust',NULL,NULL,NULL,NULL,'2016-01-25 13:48:34',NULL,NULL,'FgtmNNEhwWPKdovipYsWIgxwaYuHnbjZyynCBLnlbtMAuvlyss',NULL,NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dev_tech`
--

DROP TABLE IF EXISTS `dev_tech`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dev_tech` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dev_id` int(11) DEFAULT NULL,
  `tech_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_dev_tech_developer_id` (`dev_id`),
  KEY `FK_dev_tech_technology_id` (`tech_id`),
  CONSTRAINT `FK_dev_tech_developer_id` FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`),
  CONSTRAINT `FK_dev_tech_technology_id` FOREIGN KEY (`tech_id`) REFERENCES `technology` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=862;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dev_tech`
--

LOCK TABLES `dev_tech` WRITE;
/*!40000 ALTER TABLE `dev_tech` DISABLE KEYS */;
INSERT INTO `dev_tech` VALUES (1,1,2),(2,1,5),(3,2,2),(4,2,1),(5,3,5),(6,3,8),(7,4,2),(8,4,1),(9,5,6),(10,6,3),(11,6,4),(12,7,2),(13,7,6),(14,7,7),(15,8,8),(16,8,5),(17,9,10),(18,9,11),(19,10,11);
/*!40000 ALTER TABLE `dev_tech` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `developer`
--

DROP TABLE IF EXISTS `developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `developer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `hourly` double DEFAULT NULL,
  `zone` timestamp NULL DEFAULT NULL,
  `lang` enum('en','uk-UA') DEFAULT 'en',
  `uuid` varchar(140) DEFAULT NULL,
  `reg_url` varchar(150) DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  `salt` varchar(50) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `overview` varchar(3000) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `reg_url` (`reg_url`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=1092;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `developer`
--

LOCK TABLES `developer` WRITE;
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
INSERT INTO `developer` VALUES (1,'iuliana@gmail.com','Pavaloaie','Iuliana','Pavaloaie',13.3,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Iuli',NULL,NULL,NULL),(2,'ivanov@gmail.com','Ivanov','Sergey','Ivanov',10.2,'2011-05-21 19:44:01','en',NULL,NULL,'2011-10-11 00:00:00',0,'\0','Serg',NULL,NULL,NULL),(3,'andres@gmail.com','Pinilla','Andres','Pinilla Palacios',13.3,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Andr',NULL,NULL,NULL),(4,'ian@gmail.com','Anderson','Ian','Anderson',12.4,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Ian',NULL,NULL,NULL),(5,'hajji@gmail.com','Makram','Hajji','Makram',11.9,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Hajj',NULL,NULL,NULL),(6,'stanisa@gmail.com','Koncic','Stanisa','Koncic',10.8,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Stan',NULL,NULL,NULL),(7,'ognev@gmail.com','Ognev','Ivan','Ognev',25.2,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Ogn',NULL,NULL,NULL),(8,'gupta@gmail.com','Ashish','gupta','Ashish',10.2,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','gupta',NULL,NULL,NULL),(9,'lamine@gmail.com','Jellad','Mohamed','Lamine Jellad',14.2,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Mohamed',NULL,NULL,NULL),(10,'stefan@gmail.com','Scekic','Stefan','Scekic',14.2,'2007-12-31 22:00:01','en',NULL,NULL,'2013-10-11 00:00:00',0,'\0','Scekic',NULL,NULL,NULL),(11,'mrudevich96@gmail.com','4ef4670e5feefd3f23abd4437d26833a701043e20677713bb8a63ac7839f1223','Max','Rudevich',NULL,NULL,NULL,'c69d4658-93a1-46b9-8209-df73602a9d1a',NULL,'2016-01-21 00:00:00',NULL,NULL,'uGutvCmwchECwwgkvrMtHBonhLOiJEiSVEjaepMoqSSfHefjcq',NULL,NULL,NULL),(13,'max@gmail.com','51c8b23f801d375fe1b413e8f24badd709d515e472f07bef76972a9951773df4','max','max',NULL,NULL,NULL,'c2b9eeef-e708-480f-8af0-4726715fc5df',NULL,'2016-01-21 17:24:15',NULL,NULL,'UVbQmQVjUPcRuUfXvbWpmznANlpZnczSIvadbaEGqdLYyKyVAF',NULL,NULL,NULL),(14,'zxc@gmail.com','9aa7f549a95fa796bced1068a3438918c11963c7159d0d46094ef1a067847684','zxc','zxc',NULL,NULL,NULL,NULL,NULL,'2016-01-21 17:39:37',NULL,NULL,'ipQjYmSvcrxQJHKALnhzApkKDmYSCsLmLMGGIMEnOjoMZQtZxJ',NULL,NULL,NULL),(15,'zxc@gmail.com','e6aee8f3372a168d13a3f063f3e6129b7372547f86aedbfd3181679758191c04','sad','das',NULL,NULL,NULL,'edcd518c-73de-4906-a65e-564687f7653c',NULL,'2016-01-21 17:58:55',NULL,NULL,'zyrCCyReaICZcxWukFnxYgBUmqsJJFvlfDqWucrKEfUFVWNJFK',NULL,NULL,NULL),(16,'user@gmail.com','3dc2bbbbe9f2936e42f0db60b7f753b8c2f83394f794a60c8d847a4e1eb0f790','user','user',NULL,NULL,NULL,'6a477ccb-7b0e-4446-9845-7b77a5bdb71e',NULL,'2016-01-21 19:08:18',NULL,NULL,'eRzTFKhvFvkxPdEIJKrfNpSQDOdDxNfrEKwLsaYHHyGAtyWlnL',NULL,NULL,NULL),(19,'dev@gmail.com','890845237bd1496d285e26438ea3382eb4c697aa6b31f788b25779961eb6b6aa','dev','dev',NULL,NULL,NULL,NULL,NULL,'2016-01-22 14:04:41',NULL,NULL,'jpmiymMXncXgTxiBICUSKEpcVvyRWqnnZFrGFbzEKErWWyFeNW',NULL,NULL,NULL);
/*!40000 ALTER TABLE `developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `developer_qa`
--

DROP TABLE IF EXISTS `developer_qa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `developer_qa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dev_id` int(11) DEFAULT NULL,
  `tech_id` int(11) DEFAULT NULL,
  `rate` double DEFAULT '0',
  `expire` date DEFAULT NULL,
  `is_expire` bit(1) DEFAULT b'0',
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_developer_qa_developer_id` (`dev_id`),
  KEY `FK_developer_qa_technology_id` (`tech_id`),
  CONSTRAINT `FK_developer_qa_developer_id` FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`),
  CONSTRAINT `FK_developer_qa_technology_id` FOREIGN KEY (`tech_id`) REFERENCES `technology` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `developer_qa`
--

LOCK TABLES `developer_qa` WRITE;
/*!40000 ALTER TABLE `developer_qa` DISABLE KEYS */;
/*!40000 ALTER TABLE `developer_qa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dev_id` int(11) DEFAULT NULL,
  `cust_id` int(11) DEFAULT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `rate` int(11) DEFAULT '0',
  `author` enum('dev','customer') NOT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_feedback_customer_id` (`cust_id`),
  KEY `FK_feedback_developer_id` (`dev_id`),
  CONSTRAINT `FK_feedback_customer_id` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_feedback_developer_id` FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=4096;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,4,4,'I liked this project, work was fine.',5,'dev',0,'\0'),(2,5,4,'Work was good, thanx.',5,'dev',0,'\0'),(3,4,4,'Thank you man, you are professional',5,'customer',0,'\0'),(4,4,5,'You did it correctly',4,'customer',0,'\0');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follower`
--

DROP TABLE IF EXISTS `follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follower` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dev_id` int(11) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_follower_developer_id` (`dev_id`),
  KEY `FK_follower_ordering_id` (`order_id`),
  CONSTRAINT `FK_follower_developer_id` FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`),
  CONSTRAINT `FK_follower_ordering_id` FOREIGN KEY (`order_id`) REFERENCES `ordering` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=1170;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follower`
--

LOCK TABLES `follower` WRITE;
/*!40000 ALTER TABLE `follower` DISABLE KEYS */;
INSERT INTO `follower` VALUES (1,1,'I want this job',3,0,'\0'),(2,2,'Give it to me',3,0,'\0'),(3,9,'I want to do this job!!!!',3,0,'\0'),(4,3,'I want this job',4,0,'\0'),(5,4,'Give it to me',4,0,'\0'),(6,6,'I will take this project',5,0,'\0'),(7,7,'I will not let you down!',5,0,'\0'),(8,8,'I want to do this',5,0,'\0'),(9,2,'I want to try',5,0,'\0'),(10,9,'I want to do this job!!!!',1,0,'\0'),(11,10,'It is very easy for me, i will do this.',1,0,'\0'),(12,9,'I want to do this job!!!!',2,0,'\0'),(13,10,'It is very easy for me, i will do this.',2,0,'\0'),(14,10,'It is very easy for me, i will do this.',6,0,'\0');
/*!40000 ALTER TABLE `follower` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordering`
--

DROP TABLE IF EXISTS `ordering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordering` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(120) NOT NULL,
  `pay_type` enum('hourly','fixed') DEFAULT 'fixed',
  `descr` varchar(3000) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `date` datetime NOT NULL,
  `payment` double DEFAULT NULL,
  `started` bit(1) DEFAULT b'0',
  `started_date` datetime DEFAULT NULL,
  `ended` bit(1) DEFAULT b'0',
  `ended_date` datetime DEFAULT NULL,
  `private` bit(1) DEFAULT b'0',
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  `zone` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ordering_customer_id` (`customer_id`),
  CONSTRAINT `FK_ordering_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=2730;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordering`
--

LOCK TABLES `ordering` WRITE;
/*!40000 ALTER TABLE `ordering` DISABLE KEYS */;
INSERT INTO `ordering` VALUES (1,'SMM Instagram','hourly','I need my instagram services sold, good rates for resale on my panel.',1,'2009-06-13 00:00:00',20.399999618530273,'\0',NULL,'\0',NULL,'\0',0,'\0',NULL),(2,'mt5 stuff for Stonev','hourly','will send details of project. Fairly simple EA.',2,'2015-09-23 00:00:00',18.899999618530273,'\0',NULL,'\0',NULL,'\0',0,'\0',NULL),(3,'True Mobile Website Design','fixed','This project is for a web design and development team that can build out True Mobile site for a website which is already responsive. Experienced design and development team candidates only. For this task you would do the following: 1) Using a mockup tool we will provide. Create a full integrated mockup for the current site for about 10 key pages. Once client approves the mockup, you would then design the entire site. Site has approximately 82 pages but would be condensed heavily, we would follow a theme based on a specific theme design pages such as (home, main category page (content), sub category page ( content), dinning menu page and photo gallery), all other pages would adjust to these specific page design. Mock would be done using https://www.fluidui.com/ or other design mockup tools that lets us preview the functionality. Site would view able in various devices (tablets and phones). If this meets the standards, development begins. ',3,'2009-06-13 00:00:00',1700,'','2015-12-13 00:00:00','\0',NULL,'',0,'\0',NULL),(4,'Write some Software','hourly','customized software development project',4,'2015-06-13 00:00:00',14.5,'','2015-07-01 00:00:00','','2015-08-21 00:00:00','\0',0,'\0',NULL),(5,'Malware/Redirect correction on Website','hourly','I am having a malware/redirect issue on my website that is causing it to redirect to random other websites. Also, there is not a specific page or product that causes the issue. It happens randomly throughout the site.',5,'2015-11-03 00:00:00',21.799999237060547,'','2015-11-11 00:00:00','\0',NULL,'\0',0,'\0',NULL),(6,'GA Consulting','hourly','I am searching about a consulence on Google analytics.',5,'2015-09-23 00:00:00',13.899999618530273,'\0',NULL,'\0',NULL,'\0',0,'\0',NULL);
/*!40000 ALTER TABLE `ordering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordering_technology`
--

DROP TABLE IF EXISTS `ordering_technology`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordering_technology` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `tech_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ordering_technology_ordering_id` (`order_id`),
  KEY `FK_ordering_technology_technology_id` (`tech_id`),
  CONSTRAINT `FK_ordering_technology_ordering_id` FOREIGN KEY (`order_id`) REFERENCES `ordering` (`id`),
  CONSTRAINT `FK_ordering_technology_technology_id` FOREIGN KEY (`tech_id`) REFERENCES `technology` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordering_technology`
--

LOCK TABLES `ordering_technology` WRITE;
/*!40000 ALTER TABLE `ordering_technology` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordering_technology` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `tech_id` int(11) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `multiple` bit(1) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_question_admin_id` (`admin_id`),
  KEY `FK_question_technology_id` (`tech_id`),
  CONSTRAINT `FK_question_admin_id` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `FK_question_technology_id` FOREIGN KEY (`tech_id`) REFERENCES `technology` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=1638;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'Р’ РїРµСЂРµРјРµРЅРЅРѕР№ С‚РёРїР° char РјРѕРіСѓС‚ С…СЂР°РЅРёС‚СЊСЃСЏ Р·РЅР°С‡РµРЅРёСЏ РёР· СЃР»РµРґСѓСЋС‰РµРіРѕ РґРёР°РїР°Р·РѕРЅР°',6,1,'\0',0,'\0'),(2,'Р’РѕР·РјРѕР¶РЅР° Р»Рё РїРµСЂРµРіСЂСѓР·РєР° РѕРїРµСЂР°С‚РѕСЂРѕРІ РІ Java?',6,1,'\0',0,'\0'),(3,'РљР°Рє РѕРїСЂРµРґРµР»РёС‚СЊ РґР»РёРЅСѓ РјР°СЃСЃРёРІР° myarray?',6,1,'\0',0,'\0'),(4,'РљР°РєРёРјРё СЃРїРѕСЃРѕР±РѕРј(-Р°РјРё) РјРѕР¶РµС‚ Р±С‹С‚СЊ РІС‹СЂР°Р¶РµРЅРѕ С‡РёСЃР»Рѕ 28?',6,1,'',0,'\0'),(5,'Р’С‹Р±РµСЂРёС‚Рµ РёР· РїСЂРёРІРµРґРµРЅРЅС‹С… РІСЃРµ РјРѕРґРёС„РёРєР°С‚РѕСЂС‹, РєРѕС‚РѕСЂС‹Рµ РїСЂРёРјРµРЅРёРјС‹ РґР»СЏ top-level РєР»Р°СЃСЃРѕРІ (РЅРµ РІР»РѕР¶РµРЅРЅС‹С…).',6,1,'',0,'\0'),(6,'РљР°РєР°СЏ СЃС‚СЂСѓРєС‚СѓСЂР° РґР°РЅРЅС‹С…, СЂРµР°Р»РёР·СѓСЋС‰Р°СЏ РёРЅС‚РµСЂС„РµР№СЃ Map РёСЃРїРѕР»СЊР·СѓРµС‚ РґР»СЏ СЃСЂР°РІРЅРµРЅРёСЏ РѕР±СЉРµРєС‚РѕРІ РѕРїРµСЂР°С‚РѕСЂ ==, Р° РЅРµ РјРµС‚РѕРґ equals.',6,1,'',0,'\0'),(7,'РњРѕР¶РЅРѕ Р»Рё РґРёРЅР°РјРёС‡РµСЃРєРё РјРµРЅСЏС‚СЊ СЂР°Р·РјРµСЂ РјР°СЃСЃРёРІР°?',6,1,'\0',0,'\0'),(8,'Р’С‹Р±РµСЂРёС‚Рµ РІСЃРµ РїСЂР°РІРёР»СЊРЅС‹Рµ РІР°СЂРёР°РЅС‚С‹ СЃРѕР·РґР°РЅРёСЏ РјР°СЃСЃРёРІРѕРІ.',6,1,'',0,'\0'),(9,'РњРѕР¶РЅРѕ Р»Рё РїСЂРё РѕР±СЉСЏРІР»РµРЅРёРё РєР»Р°СЃСЃР° РёСЃРїРѕР»СЊР·РѕРІР°С‚СЊ РјРѕРґРёС„РёРєР°С‚РѕСЂС‹ abstract Рё final РѕРґРЅРѕРІСЂРµРјРµРЅРЅРѕ?',6,1,'\0',0,'\0'),(10,'РЇРІР»СЏСЋС‚СЃСЏ Р»Рё РјР°СЃСЃРёРІС‹ РІ Java РѕРґРЅРѕСЂРѕРґРЅС‹РјРё?',6,1,'\0',0,'\0');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `technology`
--

DROP TABLE IF EXISTS `technology`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `technology` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=1489;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `technology`
--

LOCK TABLES `technology` WRITE;
/*!40000 ALTER TABLE `technology` DISABLE KEYS */;
INSERT INTO `technology` VALUES (1,'.NET',0,'\0'),(2,'1C',0,'\0'),(3,'C#',0,'\0'),(4,'C/C++',0,'\0'),(5,'Flash/Flex',0,'\0'),(6,'Java',0,'\0'),(7,'Javascript',0,'\0'),(8,'Mac OS/Objective C',0,'\0'),(9,'PHP',0,'\0'),(10,'Python',0,'\0'),(11,'Ruby',0,'\0');
/*!40000 ALTER TABLE `technology` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tech_id` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `pass_score` tinyint(4) DEFAULT NULL,
  `sec_per_quest` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_test_admin_id` (`admin_id`),
  KEY `FK_test_technology_id` (`tech_id`),
  CONSTRAINT `FK_test_admin_id` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `FK_test_technology_id` FOREIGN KEY (`tech_id`) REFERENCES `technology` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_question`
--

DROP TABLE IF EXISTS `test_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `test_id` int(11) DEFAULT NULL,
  `quest_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_test_question_question_id` (`quest_id`),
  KEY `FK_test_question_test_id` (`test_id`),
  CONSTRAINT `FK_test_question_question_id` FOREIGN KEY (`quest_id`) REFERENCES `question` (`id`),
  CONSTRAINT `FK_test_question_test_id` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_question`
--

LOCK TABLES `test_question` WRITE;
/*!40000 ALTER TABLE `test_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worker`
--

DROP TABLE IF EXISTS `worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `worker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `dev_id` int(11) DEFAULT NULL,
  `new_hourly` double DEFAULT NULL,
  `sum_hours` double DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_salary_developer_id` (`dev_id`),
  KEY `FK_salary_ordering_id` (`order_id`),
  CONSTRAINT `FK_salary_developer_id` FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`),
  CONSTRAINT `FK_salary_ordering_id` FOREIGN KEY (`order_id`) REFERENCES `ordering` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=2048 COMMENT='entity that represents developers on some project';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worker`
--

LOCK TABLES `worker` WRITE;
/*!40000 ALTER TABLE `worker` DISABLE KEYS */;
INSERT INTO `worker` VALUES (1,3,1,NULL,78,0,'\0'),(2,3,2,NULL,88,0,'\0'),(3,3,9,NULL,72,0,'\0'),(4,4,4,14.5,123,0,'\0'),(5,4,5,14.5,130,0,'\0'),(6,5,6,21.799999237060547,178,0,'\0'),(7,5,7,21.799999237060547,171,0,'\0'),(8,5,8,21.799999237060547,164,0,'\0');
/*!40000 ALTER TABLE `worker` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-28 22:06:33
