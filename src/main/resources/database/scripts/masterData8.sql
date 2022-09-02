--liquibase formatted sql
--changeset {authorName}:{id}

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('29','DOC_TEMPLATE_PATH','/opt/docTemplate','DOC_TEMPLATE_PATH','','1','Sysadmin',now());

