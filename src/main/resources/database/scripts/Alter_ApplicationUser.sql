--liquibase formatted sql
--changeset {authorName}:{id}

alter table applicationuser Add Column madian_surname varchar(250);
alter table applicationuser Add Column default_surname varchar(250);
alter table applicationuser Add Column gender varchar(10) NOT NULL DEFAULT 'Male';