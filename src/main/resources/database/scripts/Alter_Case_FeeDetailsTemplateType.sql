--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casefee` Modify `TemplateType` varchar(255) NULL;