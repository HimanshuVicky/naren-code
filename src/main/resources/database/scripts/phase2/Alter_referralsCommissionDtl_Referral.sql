--liquibase formatted sql
--changeset {authorName}:{id}

alter table `applicationuser` Add Column ReferalUserId bigint(20);

ALTER TABLE `applicationuser`
ADD CONSTRAINT `fk_applicationuser__ReferalUserId` FOREIGN KEY (`ReferalUserId`) REFERENCES `userlogin` (`Id`);