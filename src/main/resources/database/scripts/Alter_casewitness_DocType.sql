--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casewitness` CHANGE COLUMN `DocumentType` `DocumentType` varchar(250) NULL;

ALTER TABLE casewitness CHANGE COLUMN `DocNumber` `DocNumber`  varchar(250) NULL;