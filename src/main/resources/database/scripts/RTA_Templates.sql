--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `rtatemplates` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(250) NOT NULL,
  `TemplateType` varchar(250) DEFAULT NULL,
  `TemplateName` varchar(250) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


