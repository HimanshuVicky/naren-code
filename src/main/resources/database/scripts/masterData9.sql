--liquibase formatted sql
--changeset {authorName}:{id}

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('30','AZ_S3_BUCKET_NAME','assignsecurities','AZ_S3_BUCKET_NAME','','1','Sysadmin',now());

