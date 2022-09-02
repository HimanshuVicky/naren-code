--liquibase formatted sql
--changeset {authorName}:{id}

insert into `typeofusers` (`ID`, `NAME`) values('0000000008','CA');

ALTER TABLE `case`
ADD `CharteredAccountantId` bigint(20) NULL AFTER `NotaryPartnerId`;


