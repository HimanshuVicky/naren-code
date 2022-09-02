--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE applicationuser Modify column UserType varchar(20) NOT NULL;