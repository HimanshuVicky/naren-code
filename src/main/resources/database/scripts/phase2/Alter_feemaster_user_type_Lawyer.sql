--liquibase formatted sql
--changeset {authorName}:{id}

UPDATE feemaster SET userTypeId=4 WHERE id IN (29);

CREATE TABLE `lawyerCaseCommentsDtl` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Comment` varchar(450) NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_lawyerCaseDtl_case_Id` (`CaseId`),
  CONSTRAINT `fk_lawyerCaseDtl_case_Id` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;