--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE property CHANGE COLUMN `CURRENT_VALUE` `CURRENT_VALUE` varchar(500) NULL;
ALTER TABLE property CHANGE COLUMN `ORIG_VALUE` `ORIG_VALUE` varchar(500) NULL;

