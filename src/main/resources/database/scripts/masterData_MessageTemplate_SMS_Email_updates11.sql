--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE message_template SET message='Findmymoney RTA Letter generated' 
WHERE code='CASE_RTALETTER_GEN_EMAIL_SUB';
