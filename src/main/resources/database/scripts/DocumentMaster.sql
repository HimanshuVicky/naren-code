--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `documentmaster` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Particulars` varchar(250)  NOT NULL,
  `Type` varchar(250)  NOT NULL,
  `UploadedOrGenerated` varchar(250)  NOT NULL,
   TempOrder bigint(20) NOT NULL DEFAULT 0,
   `Action` varchar(250)  NULL,
   `Process` varchar(250)  NULL,
   `Link` varchar(250)  NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

