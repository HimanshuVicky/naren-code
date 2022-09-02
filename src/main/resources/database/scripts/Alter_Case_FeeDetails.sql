--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casefee` ADD `FeeFor` varchar(250) DEFAULT NULL AFTER TemplateType;
ALTER TABLE `casefee` ADD `FeeType` varchar(250) NOT NULL DEFAULT 'FixValue' AFTER FeeFor;
ALTER TABLE `casefee` ADD `isGSTApplicable` tinyint(1) DEFAULT 0;
ALTER TABLE `casefee` ADD `ReceivedFeeValue` double NULL;

