--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `esign_surepass_clientids` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) NOT NULL,
  `ClientId` varchar(250) NOT NULL,
  `ReqType` varchar(250) NULL,
  `ReqStatus` varchar(250) NULL,
  `Remarks` varchar(250) NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `fk_esign_surepass_clientids__userlogin_Id` (`UserId`),
  CONSTRAINT `fk_esign_surepass_clientids__userlogin_Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
