--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case`
ADD `PanVerified` tinyint(1) NULL default 1;



