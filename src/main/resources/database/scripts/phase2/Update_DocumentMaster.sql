--liquibase formatted sql
--changeset {authorName}:{id}

UPDATE documentmaster dm SET dm.type = dm.particulars;