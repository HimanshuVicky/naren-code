--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE document ADD ContentType varchar(200);
