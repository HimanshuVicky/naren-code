--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `caseaccountdetails` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `DematAccountNumber` varchar(250)  NOT NULL,
  `IEPFUserName` varchar(250)  NOT NULL,
  `IEPFPassword` varchar(250)  NOT NULL,
  `RTAContact` varchar(250)  NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_caseaccountdetails__CaseId` (`CaseId`),
  CONSTRAINT `fk_caseaccountdetails__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

