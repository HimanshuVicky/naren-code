--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case` CHANGE  CancelCheckDoccumentId CancelChequeDoccumentId bigint(20);