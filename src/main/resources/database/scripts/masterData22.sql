--liquibase formatted sql
--changeset {authorName}:{id}

update statusmaster set Stage='Stage 1' where Stage = 'Stage 1 ';