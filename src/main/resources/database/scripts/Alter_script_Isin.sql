--liquibase formatted sql
--changeset {authorName}:{id}


ALTER TABLE script ADD `IsinCode` varchar(255) NULL AFTER `SecurityCode`;
