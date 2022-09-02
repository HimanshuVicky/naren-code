--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE generated_documents_required Modify column DocumentId bigint(20) NULL;