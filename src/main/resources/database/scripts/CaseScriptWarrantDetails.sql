--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `case_script_warrant_detls` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseScriptId` bigint(20) NOT NULL,
  `WarrantNo` varchar(150) NOT NULL,
  `Amount` double NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_case_script_warrant_Detls_CaseScriptId` (`CaseScriptId`),
  CONSTRAINT `fk_case_script_warrant_Detls_CaseScriptId` FOREIGN KEY (`CaseScriptId`) REFERENCES `casescript` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;