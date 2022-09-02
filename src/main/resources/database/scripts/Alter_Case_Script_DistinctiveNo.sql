--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casescript`
DROP COLUMN `DistinctiveNoFrom`;

ALTER TABLE `casescript`
DROP COLUMN `DistinctiveNoTo`;

ALTER TABLE `casescript`
ADD `DistinctiveNos` varchar(250) NULL AFTER `CertNo`;


