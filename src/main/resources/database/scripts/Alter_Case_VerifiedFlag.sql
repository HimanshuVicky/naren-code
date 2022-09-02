--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case`
ADD `AssignLawyerId` bigint(20) NULL AFTER `FranchiseId`;

ALTER TABLE `case`
ADD CONSTRAINT `fk_case__FranchiseId` FOREIGN KEY (`FranchiseId`) REFERENCES `userlogin` (`Id`);

ALTER TABLE `case`
ADD `IsFeeProcessed` tinyint(1) NOT NULL DEFAULT '0';
 
ALTER TABLE `case`
ADD `IsWintessInfoReceived` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE `case`
ADD `IsSuretyInfoReceived` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE `case`
ADD `IsCustomerDocumentsRequiredVerified` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE `case`
ADD `IsGeneratedDocumentsRequiredVerified` tinyint(1) NOT NULL DEFAULT '0';



