--liquibase formatted sql
--changeset {authorName}:{id}

alter table applicationuser Add Column ReferalFranchiseId bigint(20);

ALTER TABLE `applicationuser`
ADD CONSTRAINT `fk_applicationuser__DocMasterId` FOREIGN KEY (`ReferalFranchiseId`) REFERENCES `franchise` (`Id`);