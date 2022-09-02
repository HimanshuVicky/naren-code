--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE applicationuser Modify column nonCustomerPin varchar(4) NULL;