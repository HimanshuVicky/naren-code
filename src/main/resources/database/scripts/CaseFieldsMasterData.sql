--liquibase formatted sql
--changeset {authorName}:{id}


insert into `dm_obj_template` (`ID`, `LOCALE_CODE`, `OBJ_ID`, `TEMPLATE_BYTE`) values('3','en_US','3','PK');

insert into `dm_obj_def` (`ID`, `CODE`, `MAX_PARENT_ROWS`, `NAME`, `OBJECT_PER_FILE`, `STATUS_ID`, `IS_MAIN_OBJECT_YN`, `DATE_CREATED`, `DATE_MODIFIED`, `CREATED_BY`, `MODIFIED_BY`, `OBJ_ID`) 
values('3','CaseFields','0','CaseFields','0','90796','0',now(),now(),'System','System','3');


insert into `dm_obj_conf` (`ID`, `BO_MODEL_NAME`, `BUS_DATA_PROC_CLASS`, `BUS_DATA_VALIDATOR_CLASS`, `DL_DATA_PROC_CLASS`, `DATA_START_ROW_NO`, `ERROR_INDEX_NO`, `HEADER_ROW_INDEX`, `INPUT_FILE_FORMAT`, `IS_ACTION_COLUMN_REQUIRED`, `IS_REQUIRED`, `OBJECT_ASSEMBLER_CLASS`, `DIS_ASSEMBLER_CLASS_NAME`, `OBJECT_CODE`, `OBJECT_TYPE`, `PARENT_REF_KEY_COL_IDX`, `PRIMARY_KEY_COL_IDX`, `SEQ_NO`, `TAB_OR_FILE_NAME`)
values('3','com.assignsecurities.bean.CaseFieldsBean','com.assignsecurities.service.impl.CaseFieldsProcessor','com.assignsecurities.service.impl.CaseFieldsValidator','com.assignsecurities.service.impl.CaseFieldsDLProcessor','5','17','4','xlsx','1','1','com.assignsecurities.service.impl.CaseFieldsDataAssembler','com.assignsecurities.service.impl.CaseFieldsDataAssembler','CaseFields',NULL,'1,3','1,3','2','caseFields##');


insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
values('41',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Action','0','1',NULL,NULL,'32',NULL,NULL,'dlAction','3','Add','0','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
values('42',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Reference Number','1','1',NULL,NULL,'255',NULL,NULL,'referenceNumber','3','NC25','1','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('43',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Sr. No.','1','1',NULL,NULL,'255',NULL,NULL,'srNo','3','11','2','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('44',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Field Name','1','1',NULL,NULL,'255',NULL,NULL,'fieldName','3','Name of  Applicant 2','3','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('45',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Field Value','1','1',NULL,NULL,'255',NULL,NULL,'fieldValue','3','Ram Kumar','3','1');
