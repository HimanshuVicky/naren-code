--liquibase formatted sql
--changeset {authorName}:{id}

alter table applicationuser Add Column nonCustomerPin int(4) DEFAULT '0000';
