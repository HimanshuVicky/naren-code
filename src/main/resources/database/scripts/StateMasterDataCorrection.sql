--liquibase formatted sql
--changeset {authorName}:{id}
delete from statemaster;

insert into statemaster (State_Code,State_Union_Territory)  
select distinct State_Code,State_Union_Territory from citymaster;