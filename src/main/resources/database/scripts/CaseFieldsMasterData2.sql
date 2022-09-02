--liquibase formatted sql
--changeset {authorName}:{id}

update dm_obj_attribute_dtl set SEQUENCE_NUMBER=4,IS_REQUIRED=0 where ATTRIBUTE_HEADER_NAME='Field Value';

update dm_obj_attribute_dtl set IS_REQUIRED=0 where ATTRIBUTE_HEADER_NAME='Sr. No.';
