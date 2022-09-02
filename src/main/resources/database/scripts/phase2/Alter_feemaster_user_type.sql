--liquibase formatted sql
--changeset {authorName}:{id}

alter table feemaster Add Column userTypeId bigint(20);

UPDATE feemaster SET userTypeId=1 WHERE id IN (20,21,22,30,31);
UPDATE feemaster SET userTypeId=7 WHERE id IN (23,24,25,26,27,28);
UPDATE feemaster SET userTypeId=1 WHERE id IN (29);