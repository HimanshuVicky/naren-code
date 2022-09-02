--liquibase formatted sql
--changeset {authorName}:{id}
update documentmaster set Type='Death Certificate 1' where Particulars = 'Death Certificate 1';

update documentmaster set Type='Death Certificate 2' where Particulars = 'Death Certificate 2';