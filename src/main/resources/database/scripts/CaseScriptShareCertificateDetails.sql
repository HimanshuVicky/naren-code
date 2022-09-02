--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `case_script_share_cert_detls` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseScriptId` bigint(20) NOT NULL,
  `CertificateNo` varchar(50) NOT NULL,
  `DistinctiveNo` varchar(250) NOT NULL,
  `Quantity` double NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_case_script_share_Cert_Detls__CaseScriptId` (`CaseScriptId`),
  CONSTRAINT `fk_case_script_share_Cert_Detls__CaseScriptId` FOREIGN KEY (`CaseScriptId`) REFERENCES `casescript` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;