--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `courier_dtl` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `City` varchar(50) NULL,
  `url` varchar(250) NULL,
  PRIMARY KEY (`ID`)
);

CREATE TABLE `case_courierdtl` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caseId` bigint(20) NOT NULL,
  `courierId` bigint(20) NOT NULL,  
  `referncenumber` varchar(50) NOT NULL,
  `receiptDate` date NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_case_courierdtl__CaseId` (`caseId`),
  CONSTRAINT `fk_courierdtl__CaseId` FOREIGN KEY (`caseId`) REFERENCES `case` (`Id`),
  KEY `fk_courierdtl__courierId` (`courierId`),
  CONSTRAINT `fk_courierdtl__courierId` FOREIGN KEY (`courierId`) REFERENCES `courier_dtl` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

