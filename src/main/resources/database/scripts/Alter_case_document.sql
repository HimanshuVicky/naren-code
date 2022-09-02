--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casedocument`
ADD `DocMasterId` bigint(20) NULL;

ALTER TABLE `casedocument`
ADD CONSTRAINT `fk_casedocument__DocMasterId` FOREIGN KEY (`DocMasterId`) REFERENCES `documentmaster` (`Id`);

