--liquibase formatted sql
--changeset {authorName}:{id}


CREATE TABLE `role` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `CODE` varchar(50) NOT NULL,
 `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
);


CREATE TABLE `applicationfeature` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `CODE` varchar(50) NOT NULL,
  `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
);


CREATE TABLE `userrole` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleId` bigint(20) NOT NULL,
  `UserId` bigint(20) NOT NULL,
  `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`Id`),
  KEY `userrole_role_Id` (`roleId`),
  CONSTRAINT `userrole_role_Id` FOREIGN KEY (`roleId`) REFERENCES `role` (`Id`),
  KEY `userrole_userId_Id` (`UserId`),
  CONSTRAINT `userrole_userId_Id` FOREIGN KEY (`UserId`) REFERENCES `applicationuser` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `applicationfeaturerole` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleId` bigint(20) NOT NULL,
  `applicationfeatureId` bigint(20) NOT NULL,
  `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`Id`),
  KEY `applicationfeaturerole_role_Id` (`roleId`),
  CONSTRAINT `applicationfeaturerole_role_Id` FOREIGN KEY (`roleId`) REFERENCES `role` (`Id`),
  KEY `applicationfeaturerole_applicationfeatureId` (`applicationfeatureId`),
  CONSTRAINT `applicationfeaturerole_applicationfeatureId` FOREIGN KEY (`applicationfeatureId`) REFERENCES `applicationfeature` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;







