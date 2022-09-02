--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casefee` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `TemplateType` varchar(250)  NOT NULL,
  `FeeValue` double NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_casefee__CaseId` (`CaseId`),
  CONSTRAINT `fk_casefee__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

