--liquibase formatted sql
--changeset {authorName}:{id}
insert into `property` (`Id`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `DATE_MODIFIED`, `MODIFIED_BY`) 
values('35','SMS_SERVICE_ON_OFF','ON','ON','','1','2021-06-06 13:58:12','Sysadmin');