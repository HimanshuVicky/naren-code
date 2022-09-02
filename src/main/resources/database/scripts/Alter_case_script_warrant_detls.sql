--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case_script_warrant_detls`
ADD `Year` INT NULL AFTER `WarrantNo`;

