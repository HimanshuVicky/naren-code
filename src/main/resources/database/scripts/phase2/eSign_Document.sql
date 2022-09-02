--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `esign_document` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) NOT NULL,
  `DocumentId` bigint(20) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `fk_esign_document__userlogin_Id` (`UserId`),
  CONSTRAINT `fk_esign_document__userlogin_Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`),
    KEY `fk_esign_document__DocumentId` (`DocumentId`),
  CONSTRAINT `fk_esign_document__DocumentId` FOREIGN KEY (`DocumentId`) REFERENCES `document` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
