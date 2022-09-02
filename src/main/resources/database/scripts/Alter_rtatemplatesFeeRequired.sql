--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE rtatemplates ADD `IsFeeRequired` tinyint(1) NOT NULL DEFAULT '0';
