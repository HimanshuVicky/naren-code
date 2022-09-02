--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casedocument`
ADD `UploadType`  varchar(30) NULL;