--liquibase formatted sql
--changeset {authorName}:{id}
ALTER TABLE `case` MODIFY DigitalSignVerificationReference VARCHAR(255) null;
