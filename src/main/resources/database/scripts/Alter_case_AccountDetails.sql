--liquibase formatted sql
--changeset {authorName}:{id}
ALTER TABLE `caseaccountdetails`
ADD `RtaRefNo` varchar(450) NULL;