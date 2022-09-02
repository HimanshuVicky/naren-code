--liquibase formatted sql
--changeset {authorName}:{id}

update feemaster set TempOrder=1 where FeeFor='Advance Fee';
update feemaster set TempOrder=2 where FeeFor='Agreement Fees';