CREATE DATABASE  IF NOT EXISTS `librairie` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `librairie`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: librairie
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
-- Table structure for table `appartient`
--

DROP TABLE IF EXISTS `appartient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appartient` (
  `ID_LIVRE` bigint(20) unsigned NOT NULL,
  `ID_CATEGORIE` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`ID_LIVRE`,`ID_CATEGORIE`),
  KEY `APPARTIENT_FK` (`ID_LIVRE`),
  KEY `APPARTIENT2_FK` (`ID_CATEGORIE`),
  CONSTRAINT `FK_APPARTIENT` FOREIGN KEY (`ID_LIVRE`) REFERENCES `livres` (`ID_LIVRE`),
  CONSTRAINT `FK_APPARTIENT2` FOREIGN KEY (`ID_CATEGORIE`) REFERENCES `categories` (`ID_CATEGORIE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appartient`
--

LOCK TABLES `appartient` WRITE;
/*!40000 ALTER TABLE `appartient` DISABLE KEYS */;
INSERT INTO `appartient` VALUES (105,56),(105,150),(105,157),(106,149),(106,159),(106,160),(107,158),(108,159),(109,159),(110,153),(110,161),(111,161),(111,163),(112,161),(112,164),(113,162),(113,163),(114,164);
/*!40000 ALTER TABLE `appartient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `ID_CATEGORIE` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `CAT_ID_CATEGORIE` bigint(20) unsigned DEFAULT NULL,
  `TITRE` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_CATEGORIE`),
  KEY `EST_COMPOSEE_DE_FK` (`CAT_ID_CATEGORIE`),
  CONSTRAINT `FK_EST_COMPOSEE_DE` FOREIGN KEY (`CAT_ID_CATEGORIE`) REFERENCES `categories` (`ID_CATEGORIE`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (56,NULL,'Informatique','Langages, systemes, Réseaux...'),(149,56,'.NET','Plateforme entreprise MS'),(150,56,'JAVA EE','la Plateforme open source'),(153,NULL,'Maison','tout pour la maison'),(154,153,'Menuiserie','art du bricolage'),(155,NULL,'Beaux-arts','peinture, sculture, littérature'),(156,NULL,'sports','le foot et le reste...'),(157,56,'Java','tout sur le langage Java'),(158,56,'Web','Web dynamique et statique'),(159,149,'C#','langage MS'),(160,149,'framework .NET',''),(161,153,'Décoration','interieur sympa'),(162,161,'Tapiseries',''),(163,161,'Mobilier',''),(164,NULL,'Design','Beaux objets');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editeurs`
--

DROP TABLE IF EXISTS `editeurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editeurs` (
  `ID_EDITEUR` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `RAISON_SOCIALE` varchar(255) NOT NULL,
  `RUE` varchar(255) DEFAULT NULL,
  `CODE_POSTAL` bigint(20) unsigned DEFAULT NULL,
  `VILLE` varchar(255) DEFAULT NULL,
  `PAYS` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID_EDITEUR`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editeurs`
--

LOCK TABLES `editeurs` WRITE;
/*!40000 ALTER TABLE `editeurs` DISABLE KEYS */;
INSERT INTO `editeurs` VALUES (40,'Manning',NULL,3000,'Greenwich','USA'),(91,'O Reilly','nowhere street',6830,'Greenwich','USA'),(92,'Microsoft press','MS street',6830,'Redmond','USA'),(93,'Eyrolles','5 bd St germain',75005,'Paris','France');
/*!40000 ALTER TABLE `editeurs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `livres`
--

DROP TABLE IF EXISTS `livres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `livres` (
  `ID_LIVRE` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ID_EDITEUR` bigint(20) unsigned DEFAULT NULL,
  `TITRE` varchar(255) NOT NULL,
  `RESUME_LIVRE` varchar(255) DEFAULT NULL,
  `DATE_PARUTION` date NOT NULL,
  PRIMARY KEY (`ID_LIVRE`),
  KEY `EDITE_FK` (`ID_EDITEUR`),
  CONSTRAINT `FK_EDITE` FOREIGN KEY (`ID_EDITEUR`) REFERENCES `editeurs` (`ID_EDITEUR`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `livres`
--

LOCK TABLES `livres` WRITE;
/*!40000 ALTER TABLE `livres` DISABLE KEYS */;
INSERT INTO `livres` VALUES (105,NULL,'EJB3 in action','plonger dans le monde EJB3','2015-09-17'),(106,92,'ASP.NET','le web par MS','2014-01-12'),(107,93,'PHP5','la star','2006-08-25'),(108,92,'C# pour les débutants','version 3.5','2007-11-24'),(109,92,'C# pour les pros ','version 4','2009-11-18'),(110,93,'Maisons japonaises','un livre sur les plus belles maisons du japon','2016-09-18'),(111,93,'Meubles scandinaves','IKea ne passera pas !!','2015-03-26'),(112,93,'Interieur by Starck','design et chic','2017-04-15'),(113,93,'Tapisser ces meubles','toutes les techniques','2017-02-13'),(114,93,'Designers italiens','\"les designers italiens du XXe siecle\"','2001-01-11');
/*!40000 ALTER TABLE `livres` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-12 10:23:50
