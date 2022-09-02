--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE script CHANGE COLUMN `InvestorName` `InvestorFirstName` varchar(255) NOT NULL;

ALTER TABLE script ADD `InvestorMiddleName` varchar(255) NULL AFTER `InvestorFirstName`;

ALTER TABLE script ADD `InvestorLastName` varchar(255) NOT NULL AFTER `InvestorMiddleName`;
