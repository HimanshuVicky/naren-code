--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE referralscommissiondtl MODIFY `UserId` bigint(20);
ALTER TABLE referralscommissiondtl MODIFY `FranchiseId` bigint(20);
