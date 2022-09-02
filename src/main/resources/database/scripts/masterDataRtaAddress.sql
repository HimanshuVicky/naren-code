--liquibase formatted sql
--changeset {authorName}:{id}
update dm_obj_attribute_dtl set ID=34,SEQUENCE_NUMBER=13 where ID=33 and ATTRIBUTE_HEADER_NAME='MPS';
update dm_obj_attribute_dtl set ID=33,SEQUENCE_NUMBER=12 where ID=32 and ATTRIBUTE_HEADER_NAME='Security ID';
update dm_obj_attribute_dtl set ID=32,SEQUENCE_NUMBER=11 where ID=31 and ATTRIBUTE_HEADER_NAME='ISIN Number';
update dm_obj_attribute_dtl set ID=31,SEQUENCE_NUMBER=10 where ID=30 and ATTRIBUTE_HEADER_NAME='Security Code';
update dm_obj_attribute_dtl set ID=30,SEQUENCE_NUMBER=9 where ID=29 and ATTRIBUTE_HEADER_NAME='DD Amount';
update dm_obj_attribute_dtl set ID=29,SEQUENCE_NUMBER=8 where ID=28 and ATTRIBUTE_HEADER_NAME='Email';
update dm_obj_attribute_dtl set ID=28,SEQUENCE_NUMBER=7 where ID=27 and ATTRIBUTE_HEADER_NAME='Contact Number';
update dm_obj_attribute_dtl set ID=27,SEQUENCE_NUMBER=6 where ID=26 and ATTRIBUTE_HEADER_NAME='City';
update dm_obj_attribute_dtl set ID=26,SEQUENCE_NUMBER=5 where ID=25 and ATTRIBUTE_HEADER_NAME='State';
update dm_obj_attribute_dtl set ID=25,SEQUENCE_NUMBER=4 where ID=24 and ATTRIBUTE_HEADER_NAME='Address';

insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('24',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Registrar Address','1','1',NULL,NULL,'400',NULL,NULL,'registrarAddress','2','Registraraddress','3','1');
