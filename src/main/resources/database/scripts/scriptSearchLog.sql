--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `scriptsearchlog` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(250) NOT NULL,
  `MiddleName` varchar(250) DEFAULT NULL,
  `LastName` varchar(250) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SearchBy`  bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_scriptsearchlog__SearchBy` (`SearchBy`),
  CONSTRAINT `fk_scriptsearchlog__SearchBy` FOREIGN KEY (`SearchBy`) REFERENCES `userlogin` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
