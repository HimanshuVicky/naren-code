--liquibase formatted sql
--changeset {authorName}:{id}

update statusmaster set Lawyer='NA' where  Stage='Stage 1' and Status='Waiting RTA Response';