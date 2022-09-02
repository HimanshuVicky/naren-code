--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` ADD `ReferenceNumber` varchar(250) NULL AFTER Id;

update `case` set ReferenceNumber = id;

