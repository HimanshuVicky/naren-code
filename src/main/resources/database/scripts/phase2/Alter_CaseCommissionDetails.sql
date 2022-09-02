--liquibase formatted sql
--changeset {authorName}:{id}
  
  drop TABLE `casecommissiondtl`;
  
  CREATE TABLE `casecommissiondtl` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `ReferralUserId` bigint(20) NOT NULL,
  `ReferralProcFeeComm` double DEFAULT 0,
  `ReferralAgreFeeComm`double DEFAULT 0,
  `ReferralDocumentProcFeeComm`double DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `fk_casecommissiondtl__caseId` (`CaseId`),
  CONSTRAINT `fk_casecommissiondtl__caseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
  KEY `fk_casecommissiondtl__referralUserId` (`referralUserId`),
  CONSTRAINT `fk_casecommissiondtl__referralUserId` FOREIGN KEY (`ReferralUserId`) REFERENCES `userlogin` (`Id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;