--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `referralscommissiondtl` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) NOT NULL,
  `FranchiseId` bigint(20) NOT NULL,
  `RegFee` double DEFAULT 0,
  `ProcFeeComm` double DEFAULT 0,
  `AgreFeeComm`double DEFAULT 0,
  `DocProcCommFee`double DEFAULT 0,
  `isRegfeereceived` tinyint(1) DEFAULT 0,
  `eSignAgreementStatus` int(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `fk_referralscomm__userlogin_Id` (`UserId`),
  CONSTRAINT `fk_referralscomm__userlogin_Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`),
  KEY `fk_referralscomm__franchiseId` (`franchiseId`),
  CONSTRAINT `fk_referralscomm__franchiseId` FOREIGN KEY (`FranchiseId`) REFERENCES `franchise` (`Id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
  
  
  
  CREATE TABLE `casecommissiondtl` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `ReferralUserId` bigint(20) NOT NULL,
  `ReferralFranchiseId` bigint(20) NOT NULL,
  `ReferralProcFeeComm` double DEFAULT 0,
  `ReferralAgreFeeComm`double DEFAULT 0,
  `ReferralDocumentProcFeeComm`double DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `fk_casecommissiondtl__caseId` (`CaseId`),
  CONSTRAINT `fk_casecommissiondtl__caseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
  KEY `fk_casecommissiondtl__referralUserId` (`referralUserId`),
  CONSTRAINT `fk_casecommissiondtl__referralUserId` FOREIGN KEY (`ReferralUserId`) REFERENCES `userlogin` (`Id`),
  KEY `fk_casecommissiondtl__ReferralFranchiseId` (`ReferralFranchiseId`),
  CONSTRAINT `fk_casecommissiondtl__ReferralFranchiseId` FOREIGN KEY (`ReferralFranchiseId`) REFERENCES `franchise` (`Id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;