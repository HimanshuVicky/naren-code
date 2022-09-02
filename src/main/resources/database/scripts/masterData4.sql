--liquibase formatted sql
--changeset {authorName}:{id}
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('25','SurePassSign1XCo-Ordinate','10','SurePassSign1XCo-Ordinate','','1','Sysadmin',now());

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('26','SurePassSign1YCo-Ordinate','10','SurePassUrl','','1','Sysadmin',now());

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('27','SurePassSign2XCo-Ordinate','0','SurePassSign2XCo-Ordinate','','1','Sysadmin',now());

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('28','SurePassSign2YCo-Ordinate','0','SurePassSign2YCo-Ordinate','','1','Sysadmin',now());

