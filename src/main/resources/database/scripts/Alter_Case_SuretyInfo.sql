--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `caseasuretyinfo`
ADD `ITRDocumentId` bigint(20) NULL;

ALTER TABLE `caseasuretyinfo`
ADD `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `caseasuretyinfo`
ADD `CreateBy` varchar(250) NOT NULL;

ALTER TABLE `caseasuretyinfo`
ADD CONSTRAINT `fk_caseasuretyinfo__ITRDocumentId` FOREIGN KEY (`ITRDocumentId`) REFERENCES `document` (`Id`);

