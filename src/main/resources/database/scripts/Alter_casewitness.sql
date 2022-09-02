--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casewitness`
ADD `Address` varchar(400) NULL AFTER  `LastName`,
ADD `City` varchar(200) NULL AFTER `Address`,
ADD `ContactNumber` varchar(15) NULL AFTER `City`;

