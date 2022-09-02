--liquibase formatted sql
--changeset {authorName}:{id}

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('21','DEFAULT_APPLICATION_FEE_TYPE','PERCENT','Application Fee type to be defaulted.','','1','Sysadmin',now());

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('22','DEFAULT_APPLICATION_FEE','5','Application Fee to be defaulted.','','1','Sysadmin',now());

