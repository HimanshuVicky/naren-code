--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` Modify `Status` varchar(30) NOT NULL;

