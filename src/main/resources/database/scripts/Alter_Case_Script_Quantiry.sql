--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casescript`
ADD `Quantity` varchar(250) NULL AFTER `DistinctiveNos`;


