CREATE DATABASE  IF NOT EXISTS `moviestore` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `moviestore`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: moviestore
-- ------------------------------------------------------
-- Server version	5.7.9-log

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `street` varchar(80) NOT NULL DEFAULT '',
  `zip` int(11) NOT NULL DEFAULT '0',
  `phone` varchar(80) NOT NULL,
  `state` varchar(80) NOT NULL,
  `city` varchar(80) NOT NULL,
  PRIMARY KEY (`street`,`zip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES ('123 Sun Street',12345,'111-111-1111','WV','Fairmont'),('30 Walnut Street',66558,'111-111-1113','NY','New York City'),('45 New Ave',45589,'111-111-1112','CA','Sacremento'),('455 Last Avenue',56789,'111-111-1115','OR','Portland'),('456 Wut Street',12345,'222-222-2222','WV','Fairmont'),('5 Ford Rd.',45678,'222-222-2225','SD','Sioux Falls'),('67 Hop Drive',59895,'222-222-2223','OH','Clevend'),('75 Locust Ave',56495,'222-222-2224','NJ','Newark'),('78 Chemistry St.',22565,'111-111-1114','SC','Charleston'),('78 Mordor Rd.',66666,'222-222-2226','ND','Mordor');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `belongs_to_genre`
--

DROP TABLE IF EXISTS `belongs_to_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `belongs_to_genre` (
  `gName` varchar(80) NOT NULL DEFAULT '',
  `rid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`gName`,`rid`),
  KEY `rid` (`rid`),
  CONSTRAINT `belongs_to_genre_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `rentals` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `belongs_to_genre`
--

LOCK TABLES `belongs_to_genre` WRITE;
/*!40000 ALTER TABLE `belongs_to_genre` DISABLE KEYS */;
INSERT INTO `belongs_to_genre` VALUES ('Action',201),('Action',202),('Action',203),('Comedy',203),('Action',204),('Horror',205),('Historical Fiction',206),('Drama',207),('Romance',207),('Drama',208),('Historical Fiction',208),('First Person Shooter',209),('Survival Horror',210),('Grand Strategy',211),('MOBA',212),('MOBA',213),('First Person Shooter',214),('First Person Shooter',215),('Role Playing Game',215);
/*!40000 ALTER TABLE `belongs_to_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `director`
--

DROP TABLE IF EXISTS `director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `director` (
  `pid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pid`),
  CONSTRAINT `director_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `person` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `director`
--

LOCK TABLES `director` WRITE;
/*!40000 ALTER TABLE `director` DISABLE KEYS */;
INSERT INTO `director` VALUES (101),(103),(104),(107);
/*!40000 ALTER TABLE `director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game` (
  `rid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rid`),
  CONSTRAINT `game_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `rentals` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (209),(210),(211),(212),(213),(214),(215);
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `has_address`
--

DROP TABLE IF EXISTS `has_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `has_address` (
  `pid` int(11) NOT NULL DEFAULT '0',
  `street` varchar(80) NOT NULL DEFAULT '',
  `zip` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pid`,`street`,`zip`),
  KEY `street` (`street`,`zip`),
  CONSTRAINT `has_address_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `person` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `has_address_ibfk_2` FOREIGN KEY (`street`, `zip`) REFERENCES `address` (`street`, `zip`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `has_address`
--

LOCK TABLES `has_address` WRITE;
/*!40000 ALTER TABLE `has_address` DISABLE KEYS */;
INSERT INTO `has_address` VALUES (101,'123 Sun Street',12345),(106,'30 Walnut Street',66558),(115,'30 Walnut Street',66558),(104,'45 New Ave',45589),(112,'45 New Ave',45589),(110,'455 Last Avenue',56789),(103,'456 Wut Street',12345),(109,'5 Ford Rd.',45678),(105,'67 Hop Drive',59895),(107,'75 Locust Ave',56495),(111,'78 Chemistry St.',22565),(113,'78 Chemistry St.',22565),(114,'78 Chemistry St.',22565),(102,'78 Mordor Rd.',66666),(108,'78 Mordor Rd.',66666);
/*!40000 ALTER TABLE `has_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `has_won_award`
--

DROP TABLE IF EXISTS `has_won_award`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `has_won_award` (
  `aTitle` varchar(50) NOT NULL DEFAULT '',
  `rid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rid`,`aTitle`),
  CONSTRAINT `has_won_award_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `movie` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `has_won_award`
--

LOCK TABLES `has_won_award` WRITE;
/*!40000 ALTER TABLE `has_won_award` DISABLE KEYS */;
INSERT INTO `has_won_award` VALUES ('Best Sound',203),('Best Supporting',204),('Best Visual effects',204),('Best Costume Design',205),('Best Editing',207),('Best Costume Design',208),('Best Directing',208),('Best Picture',208),('Best Production',208);
/*!40000 ALTER TABLE `has_won_award` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mcast`
--

DROP TABLE IF EXISTS `mcast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mcast` (
  `pid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pid`),
  CONSTRAINT `mcast_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `person` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mcast`
--

LOCK TABLES `mcast` WRITE;
/*!40000 ALTER TABLE `mcast` DISABLE KEYS */;
INSERT INTO `mcast` VALUES (102),(105),(106),(107),(108),(109),(110),(112),(115);
/*!40000 ALTER TABLE `mcast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie` (
  `rid` int(11) NOT NULL DEFAULT '0',
  `pid` int(11) NOT NULL,
  `rid_of_prequel` int(11) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `pid` (`pid`),
  KEY `rid_of_prequel` (`rid_of_prequel`),
  CONSTRAINT `movie_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `rentals` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movie_ibfk_2` FOREIGN KEY (`pid`) REFERENCES `director` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movie_ibfk_3` FOREIGN KEY (`rid_of_prequel`) REFERENCES `movie` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
INSERT INTO `movie` VALUES (201,103,NULL),(202,107,201),(203,104,NULL),(204,103,202),(205,101,NULL),(206,107,NULL),(207,101,NULL),(208,104,NULL);
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `pid` int(11) NOT NULL DEFAULT '0',
  `pname` varchar(80) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (101,'Jimmy John'),(102,'Thor Odinson'),(103,'Jimmy Droptables'),(104,'Sammy Sosa'),(105,'Rick Grimes'),(106,'Morgen Freeman'),(107,'Clint Eastwood'),(108,'B.J. Blascowtiz'),(109,'Andy Franklin'),(110,'Charlie Kelly'),(111,'Dennis Reynolds'),(112,'Mac Maccarroni'),(113,'Dee Reynolds'),(114,'Frank Reynolds'),(115,'Danny DeVito');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plays_on_platform`
--

DROP TABLE IF EXISTS `plays_on_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plays_on_platform` (
  `rid` int(11) NOT NULL DEFAULT '0',
  `platName` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`rid`,`platName`),
  CONSTRAINT `plays_on_platform_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `game` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plays_on_platform`
--

LOCK TABLES `plays_on_platform` WRITE;
/*!40000 ALTER TABLE `plays_on_platform` DISABLE KEYS */;
INSERT INTO `plays_on_platform` VALUES (209,'Playstation 4'),(209,'Xbox One'),(210,'Wii U'),(211,'Playstation 3'),(212,'Xbox One'),(213,'Playstation 4'),(213,'Xbox One'),(214,'Xbox 360'),(215,'Playstation 4'),(215,'Xbox One');
/*!40000 ALTER TABLE `plays_on_platform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rentals`
--

DROP TABLE IF EXISTS `rentals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rentals` (
  `rid` int(11) NOT NULL DEFAULT '0',
  `title` varchar(80) NOT NULL,
  `releaseDate` date DEFAULT NULL,
  `num_availible_copys` int(11) NOT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rentals`
--

LOCK TABLES `rentals` WRITE;
/*!40000 ALTER TABLE `rentals` DISABLE KEYS */;
INSERT INTO `rentals` VALUES (201,'Iron Man','2010-10-15',3),(202,'Iron Man 2','2012-01-25',3),(203,'Guardians of the Galaxy','2014-02-17',3),(204,'Iron Man 3','2014-02-22',3),(205,'The Babadook','2014-11-10',3),(206,'Captain America: The First Avenger','2013-08-10',3),(207,'The Notebook','2006-06-15',3),(208,'Saving Private Ryan','2003-09-15',3),(209,'Counter Strike: Global Offensive','2003-04-30',3),(210,'Five Nights at Freddy\'s','2014-11-30',3),(211,'Crusader Kings 2','2013-04-26',3),(212,'SMITE','2015-01-20',3),(213,'DotA 2','2013-01-20',3),(214,'HOMEFRONT','2013-06-16',3),(215,'Fallout 4','2015-11-10',1);
/*!40000 ALTER TABLE `rentals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rentals_record_rents`
--

DROP TABLE IF EXISTS `rentals_record_rents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rentals_record_rents` (
  `Trackingnum` int(11) NOT NULL DEFAULT '0',
  `rid` int(11) NOT NULL DEFAULT '0',
  `pid` int(11) NOT NULL DEFAULT '0',
  `from_Date` date NOT NULL,
  `return_Date` date DEFAULT NULL,
  PRIMARY KEY (`rid`,`pid`,`Trackingnum`),
  KEY `pid` (`pid`),
  CONSTRAINT `rentals_record_rents_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `user` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rentals_record_rents_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `rentals` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rentals_record_rents`
--

LOCK TABLES `rentals_record_rents` WRITE;
/*!40000 ALTER TABLE `rentals_record_rents` DISABLE KEYS */;
INSERT INTO `rentals_record_rents` VALUES (7001,201,101,'2015-10-01','2015-10-02'),(7002,201,114,'2015-10-05','2015-10-06'),(7003,202,105,'2015-10-09','2015-10-07'),(7004,203,110,'2015-10-10','2015-10-10'),(7005,203,111,'2015-10-11','2015-10-12'),(7006,203,113,'2015-10-12','2015-10-13'),(7007,204,113,'2015-10-14','2015-10-15'),(7008,205,110,'2015-10-14','2015-10-15'),(7009,205,111,'2015-10-16','2015-10-19'),(7010,206,105,'2015-10-16','2015-10-20'),(7011,207,115,'2015-10-17','2015-10-19'),(7012,208,101,'2015-10-18','2015-10-25'),(7013,208,111,'2015-10-18','2015-10-22'),(7014,209,105,'2015-10-21','2015-10-22'),(7015,209,115,'2015-10-25','2015-10-26'),(7016,210,101,'2015-10-25','2015-10-29'),(7018,210,110,'2015-10-29','2015-11-02'),(7017,210,111,'2015-10-27','2015-10-29'),(7019,210,113,'2015-10-29','2015-11-02'),(7020,210,115,'2015-10-29','2015-11-02'),(7021,211,115,'2015-10-30','2015-11-02'),(7023,212,110,'2015-10-30','2015-11-02'),(7022,212,114,'2015-10-30','2015-11-02'),(7028,213,101,'2015-11-11','2015-11-15'),(7025,213,105,'2015-11-01','2015-11-12'),(7030,213,110,'2015-11-12','2015-11-15'),(7024,213,111,'2015-10-31','2015-11-11'),(7026,213,113,'2015-11-09','2015-11-11'),(7027,213,114,'2015-11-11','2015-11-16'),(7029,213,115,'2015-11-11','2015-11-17'),(7031,214,101,'2015-11-12','2015-11-17'),(7032,215,105,'2015-11-12','2015-11-17'),(7033,215,114,'2015-11-13','2015-11-17');
/*!40000 ALTER TABLE `rentals_record_rents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `search`
--

DROP TABLE IF EXISTS `search`;
/*!50001 DROP VIEW IF EXISTS `search`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `search` AS SELECT 
 1 AS `rid`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `pid` int(11) NOT NULL DEFAULT '0',
  `email` varchar(80) NOT NULL,
  `is_admin` tinyint(4) DEFAULT NULL,
  `rental_quota` tinyint(4) DEFAULT '1',
  `user_password` varchar(80) NOT NULL,
  PRIMARY KEY (`pid`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `person` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (101,'j@john.yum',0,3,'monkey'),(105,'r@grimes.wd',0,2,'wut'),(110,'a',1,5,'a'),(111,'hi@there.bye',0,4,'password'),(113,'default@email.net',0,3,'default'),(114,'puppy@lover.yay',0,4,'puppy'),(115,'d@devito.com',1,6,'alsoadmin');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `was_in`
--

DROP TABLE IF EXISTS `was_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `was_in` (
  `pid` int(11) NOT NULL DEFAULT '0',
  `rid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pid`,`rid`),
  KEY `rid` (`rid`),
  CONSTRAINT `was_in_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `mcast` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `was_in_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `movie` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `was_in`
--

LOCK TABLES `was_in` WRITE;
/*!40000 ALTER TABLE `was_in` DISABLE KEYS */;
INSERT INTO `was_in` VALUES (102,201),(105,201),(110,202),(112,202),(102,203),(115,203),(109,204),(106,205),(108,206),(106,207),(102,208),(107,208);
/*!40000 ALTER TABLE `was_in` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'moviestore'
--

--
-- Dumping routines for database 'moviestore'
--

--
-- Final view structure for view `search`
--

/*!50001 DROP VIEW IF EXISTS `search`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `search` AS select distinct `mgr`.`rid` AS `rid` from ((`rentals` `mgr` join `belongs_to_genre` `mgb`) join `movie` `mg`) where ((`mgb`.`gName` like '%%') and (`mgb`.`rid` = `mg`.`rid`) and (`mg`.`rid` = `mgr`.`rid`)) union select distinct `r`.`rid` AS `rid` from ((((`rentals` `r` join `person` `p`) join `mcast` `c`) join `movie` `m`) join `was_in` `w`) where ((`p`.`pname` like '%%') and (`p`.`pid` = `c`.`pid`) and (`w`.`pid` = `c`.`pid`) and (`w`.`rid` = `m`.`rid`) and (`m`.`rid` = `r`.`rid`)) union select distinct `r`.`rid` AS `rid` from (((`rentals` `r` join `person` `p`) join `director` `d`) join `movie` `m`) where ((`p`.`pname` like '%%') and (`p`.`pid` = `d`.`pid`) and (`m`.`pid` = `d`.`pid`) and (`m`.`rid` = `r`.`rid`)) union select distinct `tmr`.`rid` AS `rid` from (`rentals` `tmr` join `movie` `tm`) where ((`tmr`.`title` like '%%') and (`tmr`.`rid` = `tm`.`rid`) and (`tm`.`rid` = `tmr`.`rid`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-24 16:53:25
