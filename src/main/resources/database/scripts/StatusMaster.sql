--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `statusmaster` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Stage` varchar(250)  NOT NULL,
  `Status` varchar(250)  NOT NULL,
  `Customer` varchar(50)  NOT NULL,
  `CustomerCare` varchar(50)  NOT NULL,
  `Admin` varchar(50)  NOT NULL,
  `FranchiseOwner` varchar(50)  NOT NULL,
  `Franchise User` varchar(50)  NOT NULL,
  `Lawyer` varchar(50)  NOT NULL,
  `AutoUpdate` tinyint(1) DEFAULT 0,
  `Flag` varchar(50)  NOT NULL,
  `IsActive` tinyint(1) DEFAULT 1,
   TempOrder bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

