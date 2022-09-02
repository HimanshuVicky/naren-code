--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `lawyerCaseCommentsDtl` MODIFY `Comment` VARCHAR(510);