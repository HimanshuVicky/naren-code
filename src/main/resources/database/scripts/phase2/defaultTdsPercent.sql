--liquibase formatted sql
--changeset {authorName}:{id}

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('36','DefaultTDSPercent','0','DefaultTDSPercent','','1','Sysadmin',now());
