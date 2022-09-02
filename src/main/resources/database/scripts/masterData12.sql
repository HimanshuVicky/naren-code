--liquibase formatted sql
--changeset {authorName}:{id}

update feemaster set TempOrder=0 where FeeFor='Processing Fees';