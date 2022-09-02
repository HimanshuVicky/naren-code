--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` ADD `ChequeNumber` varchar(20) DEFAULT NULL;