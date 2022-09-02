--liquibase formatted sql
--changeset {authorName}:{id}

update dm_obj_attribute_dtl set IS_REQUIRED=0 where ATTRIBUTE_HEADER_NAME='Investor Middle Name';