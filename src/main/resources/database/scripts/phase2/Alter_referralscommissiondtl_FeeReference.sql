--liquibase formatted sql
--changeset {authorName}:{id}

alter table `referralscommissiondtl` Add Column FeeReferenceKey varchar(50);
