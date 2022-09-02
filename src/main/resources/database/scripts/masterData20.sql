--liquibase formatted sql
--changeset {authorName}:{id}

update dm_obj_conf set PARENT_REF_KEY_COL_IDX='3,4,6,13,14',ERROR_INDEX_NO=18 where Id=1;
update dm_obj_conf set PARENT_REF_KEY_COL_IDX='10,11',ERROR_INDEX_NO=14 where Id=2;
