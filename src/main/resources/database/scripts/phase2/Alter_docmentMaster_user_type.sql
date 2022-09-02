--liquibase formatted sql
--changeset {authorName}:{id}

alter table documentmaster Add Column userTypeId bigint(20);

UPDATE documentmaster SET userTypeId=4 WHERE id IN (40,41,114,115,116,117);
UPDATE documentmaster SET userTypeId=7 WHERE id IN (68,69,70,71,74,75,76,80,82,83,84,85,93,99,100,101,102,112,113);
