--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casescriptques` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseScriptId` bigint(20) NOT NULL,
  `QuestionId` int(10) unsigned zerofill NOT NULL,
  `Answer` varchar(15) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_casescriptques__CaseScriptId` (`CaseScriptId`),
  CONSTRAINT `fk_casescriptques__CaseScriptId` FOREIGN KEY (`CaseScriptId`) REFERENCES `casescript` (`Id`),
  KEY `fk_casescriptques__QuestionId_Id` (`QuestionId`),
  CONSTRAINT `fk_casescriptques__QuestionId_Id` FOREIGN KEY (`QuestionId`) REFERENCES `questioner` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;