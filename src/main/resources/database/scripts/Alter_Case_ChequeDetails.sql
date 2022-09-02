--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` ADD `BankName` varchar(100) NULL AFTER `ChequeNumber`;
ALTER TABLE `case` ADD `BankAddress` varchar(350) NULL AFTER `BankName`;
ALTER TABLE `case` ADD `AccountNumber` varchar(20) NULL AFTER `BankAddress`;
ALTER TABLE `case` ADD `IfscCode` varchar(20) NULL AFTER `AccountNumber`;
