--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `statemaster` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `State_Code` varchar(250) NOT NULL,
  `State_Union_Territory` varchar(250) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;