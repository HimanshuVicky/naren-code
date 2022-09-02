--liquibase formatted sql
--changeset {authorName}:{id}


ALTER TABLE `casewitness`
DROP COLUMN `MiddleName`;

ALTER TABLE `casewitness`
DROP COLUMN `LastName`;

ALTER TABLE `casewitness`
DROP COLUMN `FirstName`;


ALTER TABLE `casewitness`
ADD `Name` varchar(450) NOT NULL AFTER  `CaseId`;