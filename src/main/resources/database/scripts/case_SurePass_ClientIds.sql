--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `case_surepass_clientids` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `ClientId` varchar(250) NOT NULL,
  `ReqType` varchar(250) NULL,
  `ReqStatus` varchar(250) NULL,
  `Remarks` varchar(250) NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `case_surepass_clientids` (`CaseId`),
  CONSTRAINT `case_surepass_clientids` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
