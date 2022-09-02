--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casewitness` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `FirstName` varchar(250) NOT NULL,
  `MiddleName` varchar(250) DEFAULT NULL,
  `LastName` varchar(250) NOT NULL,  
  `DocumentType`varchar(250) NOT NULL,
  `DocNumber` varchar(250) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreateBy` varchar(250) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_casewitness__CaseId` (`CaseId`),
  CONSTRAINT `fk_casewitness__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
