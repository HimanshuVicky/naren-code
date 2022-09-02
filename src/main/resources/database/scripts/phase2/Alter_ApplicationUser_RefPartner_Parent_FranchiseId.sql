--liquibase formatted sql
--changeset {authorName}:{id}

alter table applicationuser Add Column referalPartnerFranchiseId bigint(20) DEFAULT 0;