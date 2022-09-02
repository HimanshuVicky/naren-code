--liquibase formatted sql
--changeset {authorName}:{id}

alter table applicationuser  Add Column  `DateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP;