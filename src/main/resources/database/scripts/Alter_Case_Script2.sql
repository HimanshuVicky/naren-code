--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casescript` DROP CertNo;

ALTER TABLE `casescript` DROP DistinctiveNos;

ALTER TABLE `casescript`
ADD `isPrimaryCaseHolderDeceased` tinyint(1) DEFAULT 0;

ALTER TABLE `casescript`
ADD `isSecondayCaseHolderDeceased` tinyint(1) DEFAULT 0;


