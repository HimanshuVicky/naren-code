--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `casefieldmaster` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Field` varchar(250)  NOT NULL,
  `FieldPlaceHolder` varchar(250)  NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

