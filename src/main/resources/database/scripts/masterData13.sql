--liquibase formatted sql
--changeset {authorName}:{id}

delete from citymaster where city ='Aurangabad' and Urban_Status='C.T.' and State_Union_Territory='Maharashtra';