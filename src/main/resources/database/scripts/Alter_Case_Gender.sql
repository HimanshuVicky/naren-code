--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` ADD `Gender` varchar(10) NULL AFTER DateOfBirth;