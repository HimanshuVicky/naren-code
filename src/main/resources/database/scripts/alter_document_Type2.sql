--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `document` Modify `Type` varchar(250) NOT NULL;
