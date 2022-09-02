--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casescript` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `ScriptId` bigint(20) NOT NULL,
  `CertNo` varchar(250) DEFAULT NULL,
  `ShareNosRange` varchar(250) DEFAULT NULL,
  `PrimaryCaseHolder` varchar(400) DEFAULT NULL,
  `SecondayCaseHolder` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_casescript__CaseId` (`CaseId`),
  CONSTRAINT `fk_casescript__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
  KEY `fk_casescript__ScriptId` (`ScriptId`),
  CONSTRAINT `fk_casescript__ScriptId` FOREIGN KEY (`ScriptId`) REFERENCES `script` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
