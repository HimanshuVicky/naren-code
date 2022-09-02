--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE franchise ADD EmailId varchar(200);
ALTER TABLE franchise ADD isActive tinyint;