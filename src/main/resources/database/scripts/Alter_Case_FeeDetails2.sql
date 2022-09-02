--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casefee` ADD `RefNo` varchar(250) DEFAULT NULL AFTER FeeValue;


