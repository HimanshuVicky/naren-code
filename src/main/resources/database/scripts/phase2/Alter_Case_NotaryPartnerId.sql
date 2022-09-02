--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case`
ADD `NotaryPartnerId` bigint(20) NULL AFTER `AssignLawyerId`;



