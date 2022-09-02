--liquibase formatted sql
--changeset {authorName}:{id}

update statusmaster set Lawyer='View' where stage='Stage 1' and Status='Waiting RTA Response';