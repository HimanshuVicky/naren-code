--liquibase formatted sql
--changeset {authorName}:{id}
CREATE TABLE `case_shareholder_death_certdtl` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caseId` bigint(20) NOT NULL,
  `DeceasedName` varchar(50) NOT NULL,
  `relation` varchar(50),
  `DocumentId` bigint(20),
   PRIMARY KEY (`ID`),
  KEY `fk_case_shareholder_death_certdtl_CaseId` (`caseId`),
  CONSTRAINT `fk_case_shareholder_death_certdtl_CaseId` FOREIGN KEY (`caseId`) REFERENCES `case` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

