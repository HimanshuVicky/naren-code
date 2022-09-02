--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` ADD `DateOfBirth` date DEFAULT NULL;