--liquibase formatted sql
--changeset {authorName}:{id}


ALTER TABLE `casewitness`
DROP COLUMN `DocumentType`;

ALTER TABLE `casewitness`
DROP COLUMN `DocNumber`;


ALTER TABLE `casewitness`
ADD `AadharNumber` varchar(20) NULL AFTER  `ContactNumber`,
ADD `PanNumber` varchar(20) NULL AFTER `AadharNumber`,
ADD `AdharDocumentId` bigint(20) NOT NULL AFTER `PanNumber`,
ADD `PanDocumentId` bigint(20) NOT NULL AFTER `AdharDocumentId`;
 
ALTER TABLE `casewitness`
ADD CONSTRAINT `fk_casewitness__AdharDocumentId` FOREIGN KEY (`AdharDocumentId`) REFERENCES `document` (`Id`);

ALTER TABLE `casewitness`
ADD CONSTRAINT `fk_casewitness__PanDocumentId` FOREIGN KEY (`PanDocumentId`) REFERENCES `document` (`Id`);