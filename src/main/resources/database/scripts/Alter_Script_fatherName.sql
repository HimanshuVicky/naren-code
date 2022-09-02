--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE script CHANGE COLUMN `FatherName` `FatherName` varchar(255) NULL;