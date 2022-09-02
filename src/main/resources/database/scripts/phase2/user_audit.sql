--liquibase formatted sql
--changeset {authorName}:{id}
 CREATE TABLE `user_audit` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IpAddress` varchar(50) NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_user_audit__UserId` (`UserId`),
  CONSTRAINT `fk_user_audit__UserId` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;