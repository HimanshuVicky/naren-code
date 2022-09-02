--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `caseclientid` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `ClientId1` varchar(250) NOT NULL,
  `ClientId2` varchar(250) NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_caseclientid__CaseId` (`CaseId`),
  CONSTRAINT `fk_caseclientid__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
