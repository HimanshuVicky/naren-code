--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE rtadata ADD `RegistrarAddress` varchar(450) NULL AFTER RegistrarName;
