--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE dm_obj_import CHANGE COLUMN `FILE_BYTE` `FILE_BYTE` LONGBLOB NULL;

ALTER TABLE dm_obj_import CHANGE COLUMN `RETRY_FILE` `RETRY_FILE` LONGBLOB NULL;
