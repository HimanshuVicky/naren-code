--liquibase formatted sql
--changeset {authorName}:{id}

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('921','RTA_DOCUMENTS_UPLOADED_SMS','RTA_DOCUMENTS_UPLOADED_SMS','SMS','The RTA response has been upload for case {0}. Please verify.');