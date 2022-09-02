--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `citymaster` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `City` varchar(250) NOT NULL,
  `Urban_Status` varchar(250) NOT NULL,
  `State_Code` varchar(250) NOT NULL,
  `State_Union_Territory` varchar(250) NOT NULL,
  `District_Code` varchar(250) NOT NULL,
  `District` varchar(250) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;