--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `rta_templates_group` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `RtaName` varchar(250) NOT NULL,
  `RtaGroup` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


