--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE rtatemplates ADD TempOrder bigint(20) NOT NULL DEFAULT 0;
