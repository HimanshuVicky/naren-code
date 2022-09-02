--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case_shareholder_death_certdtl` ADD `dateOfDeath` date DEFAULT NULL;