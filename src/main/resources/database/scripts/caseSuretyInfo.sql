--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `caseasuretyinfo` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `Name` varchar(250)  NOT NULL,
  `Address` varchar(450)  NOT NULL,
  `City` varchar(250)  NOT NULL,
  `Phone` varchar(20)  NOT NULL,
  `AadharNo` varchar(20)  NOT NULL,
  `PanNo` varchar(20)  NOT NULL,
  `ITRRefNo` varchar(20)  NOT NULL,
  `AadharDocId` bigint(20) NULL,
  `PanDocId` bigint(20) NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_caseasuretyinfo__CaseId` (`CaseId`),
  CONSTRAINT `fk_caseasuretyinfo__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
  KEY `fk_caseasuretyinfo__AadharDocId` (`AadharDocId`),
  CONSTRAINT `fk_caseasuretyinfo__AadharDocId` FOREIGN KEY (`AadharDocId`) REFERENCES `document` (`Id`),
  KEY `fk_caseasuretyinfo__PanDocId` (`PanDocId`),
  CONSTRAINT `fk_caseasuretyinfo__PanDocId` FOREIGN KEY (`PanDocId`) REFERENCES `document` (`Id`),
  KEY `fk_case__PanDocId` (`PanDocId`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

