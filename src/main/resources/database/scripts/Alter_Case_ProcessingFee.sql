--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` ADD `ProcessingFee` double DEFAULT 0;