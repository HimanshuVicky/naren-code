--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casedocument` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `DocumentId` bigint(20) NOT NULL,
  `Type` varchar(250) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreateBy` varchar(250) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_casedocument__CaseId` (`CaseId`),
  CONSTRAINT `fk_casedocument__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
  KEY `fk_casedocument__DocumentId` (`DocumentId`),
  CONSTRAINT `fk_casedocument__DocumentId` FOREIGN KEY (`DocumentId`) REFERENCES `document` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
