--liquibase formatted sql
--changeset {authorName}:{id}
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=18;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=17;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=16;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=15;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=14;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=13;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=12;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=11;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=10;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=9;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=8;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=7;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=6;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=5;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=4;
update dm_obj_attribute_dtl set SEQUENCE_NUMBER=(SEQUENCE_NUMBER+1),ID=(ID+1) 
where id=3;

insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('3',NULL,NULL,NULL,'java.lang.String','-1',NULL,'ISIN Number','1','1',NULL,NULL,'255',NULL,NULL,'isinCode','1','OM123','2','1');








