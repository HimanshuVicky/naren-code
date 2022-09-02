--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casefields` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `Field` varchar(250)  NOT NULL,
  `FieldVAlue` varchar(250)  NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_CaseFields__CaseId1` (`CaseId`),
  CONSTRAINT `fk_CaseFields__CaseId1` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

