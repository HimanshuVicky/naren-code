--liquibase formatted sql
--changeset {authorName}:{id}
  
alter table case_script_warrant_detls change `Year` `Year` varchar(15);