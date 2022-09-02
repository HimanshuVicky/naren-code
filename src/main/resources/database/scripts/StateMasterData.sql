--liquibase formatted sql
--changeset {authorName}:{id}

insert into statemaster (State_Code,State_Union_Territory)  
select State_Code,State_Union_Territory from citymaster;