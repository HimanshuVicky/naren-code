--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `applicationfee` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ApplicationId` bigint(20) NOT NULL,
  `FeeFor` varchar(250)  NOT NULL,
  `FeeType` varchar(10)  NOT NULL DEFAULT 'FixValue',
  `FeeValue` double NOT NULL,
  `isGSTApplicable` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `fk_applicationfee__applicationId` (`ApplicationId`),
  CONSTRAINT `fk_applicationfee__applicationId` FOREIGN KEY (`ApplicationId`) REFERENCES `application` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

