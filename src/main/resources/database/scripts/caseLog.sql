--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `caselog` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `DocumentId` bigint(20) NULL,
  `Action` varchar(250) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreateBy` varchar(250) NOT NULL,
  `Remarks` varchar(250) NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_caselog__CaseId` (`CaseId`),
  CONSTRAINT `fk_caselog__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
  KEY `fk_caselog__DocumentId` (`DocumentId`),
  CONSTRAINT `fk_caselog__DocumentId` FOREIGN KEY (`DocumentId`) REFERENCES `document` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
