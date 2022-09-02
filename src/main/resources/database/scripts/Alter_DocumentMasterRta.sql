--liquibase formatted sql
--changeset {authorName}:{id}


ALTER TABLE documentmaster ADD RtaGroup varchar(250) NULL;
