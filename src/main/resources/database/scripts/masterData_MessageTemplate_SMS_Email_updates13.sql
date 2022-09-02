--liquibase formatted sql
--changeset {authorName}:{id}
insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`)
values('922','CASE_ASSIGNED','CASE_ASSIGNED','SMS','Dear {0}\\r\\nYour case {1} has been assigned to you.\\r\\nAIPL');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) 
values('923','CASE_CLOSED','CASE_CLOSED','SMS','Dear {0}\\r\\nYour case {1} has been closed.\\r\\nAIPL');

