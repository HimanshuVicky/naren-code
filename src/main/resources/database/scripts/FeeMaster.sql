--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `feemaster` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FeeFor` varchar(250)  NOT NULL,
  `FeeType` varchar(10)  NOT NULL DEFAULT 'FixValue',
  `isGSTApplicable` tinyint(1) DEFAULT 0,
   TempOrder bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

