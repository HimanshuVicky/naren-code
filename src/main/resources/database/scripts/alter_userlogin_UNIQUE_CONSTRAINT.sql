--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE userlogin
ADD CONSTRAINT UC_userlogin_MobileNo UNIQUE (MobileNo);