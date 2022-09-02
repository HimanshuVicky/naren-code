--liquibase formatted sql
--changeset {authorName}:{id}

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('31','TOKEN_CLEANER_IN_MINUTES','30','TOKEN_CLEANER_IN_MINUTES','','1','Sysadmin',now());

