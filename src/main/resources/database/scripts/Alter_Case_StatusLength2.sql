--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` Modify `Status` varchar(50) NOT NULL;

