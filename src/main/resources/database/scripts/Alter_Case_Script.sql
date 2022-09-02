--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casescript`
ADD `DistinctiveNoFrom` bigint(20) NULL AFTER `CertNo`;

ALTER TABLE `casescript`
ADD `DistinctiveNoTo` bigint(20) NULL AFTER `DistinctiveNoFrom`;

ALTER TABLE `casescript`
ADD `FaceValue` bigint(20) NULL AFTER `DistinctiveNoTo`;

ALTER TABLE `casescript` DROP ShareNosRange;

